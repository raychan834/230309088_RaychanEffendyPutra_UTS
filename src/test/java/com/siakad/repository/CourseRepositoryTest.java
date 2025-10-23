package com.siakad.repository;

// Impor kelas-kelas utama
import com.siakad.exception.*;
import com.siakad.model.Course;
import com.siakad.model.Enrollment;
import com.siakad.model.Student;

// Impor anotasi JUnit dan Mockito
import com.siakad.service.EnrollmentService;
import com.siakad.service.GradeCalculator;
import com.siakad.service.NotificationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

// Impor method 'static' dari Mockito dan Assertions
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Menggunakan MockitoExtension untuk mengaktifkan anotasi @Mock dan @InjectMocks
 */
@ExtendWith(MockitoExtension.class)
class EnrollmentServiceTest {

    // --- SETUP UNTUK STUB & MOCK ---
    // @Mock membuat objek palsu dari dependensi
    @Mock
    private StudentRepository studentRepository;
    @Mock
    private CourseRepository courseRepository;
    @Mock
    private NotificationService notificationService;

    // Kita gunakan GradeCalculator ASLI karena sudah diuji 100%
    private GradeCalculator gradeCalculator;

    // @InjectMocks membuat objek ASLI yang akan diuji
    // dan menyuntikkan semua @Mock ke dalamnya
    @InjectMocks
    private EnrollmentService enrollmentService;

    // Data palsu untuk digunakan di berbagai tes
    private Student fakeStudent;
    private Course fakeCourse;

    @BeforeEach
    void setUp() {
        // Inisialisasi GradeCalculator asli
        gradeCalculator = new GradeCalculator();

        // Inisialisasi ulang service dengan mock DAN GradeCalculator asli
        enrollmentService = new EnrollmentService(
                studentRepository,
                courseRepository,
                notificationService,
                gradeCalculator
        );

        // Siapkan data palsu
        fakeStudent = new Student("S001", "Nama", "email@test.com", "RKS", 5, 3.5, "ACTIVE");
        fakeCourse = new Course("CS-101", "Algo", 3, 50, 49, "Dosen");
    }

    // --- SOAL 1B: UNIT TEST MENGGUNAKAN STUB ---
    //
    // STUB: Fokus pada PENGATURAN DATA (State Verification).
    // Kita "men-stub" atau "memprogram" repositori (via 'when().thenReturn()')
    // untuk mengembalikan data palsu yang spesifik.
    // Kita MENGUJI RETURN VALUE (misal: 'true', 'false', atau 'Exception').
    //
    // Di soal ini, method 'validateCreditLimit' dan 'dropCourse' diuji sebagai Stub.
    //

    @Test
    @DisplayName("SOAL 1B (Stub): Validasi SKS Diterima")
    void testValidateCreditLimitSuccess_AsStub() {
        // 1. Arrange (Persiapan STUB)
        // Kita program 'studentRepository' untuk mengembalikan 'fakeStudent' (IPK 3.5)
        when(studentRepository.findById("S001")).thenReturn(fakeStudent);

        // 2. Act
        boolean result = enrollmentService.validateCreditLimit("S001", 24);

        // 3. Assert (Validasi HASIL/STATE)
        assertTrue(result, "IPK 3.5 harusnya boleh ambil 24 SKS");
    }

    @Test
    @DisplayName("SOAL 1B (Stub): Validasi SKS Ditolak")
    void testValidateCreditLimitFailed_AsStub() {
        // 1. Arrange (Persiapan STUB)
        fakeStudent.setGpa(2.1); // Ubah IPK jadi 2.1
        when(studentRepository.findById("S001")).thenReturn(fakeStudent);

        // 2. Act
        boolean result = enrollmentService.validateCreditLimit("S001", 21);

        // 3. Assert (Validasi HASIL/STATE)
        assertFalse(result, "IPK 2.1 tidak boleh ambil 21 SKS");
    }

    @Test
    @DisplayName("SOAL 1B (Stub): Validasi SKS Error (Student Tidak Ditemukan)")
    void testValidateCreditLimitStudentNotFound_AsStub() {
        // 1. Arrange (Persiapan STUB)
        // Kita program 'studentRepository' untuk mengembalikan 'null'
        when(studentRepository.findById("S999")).thenReturn(null);

        // 2. Act & 3. Assert (Validasi EXCEPTION)
        assertThrows(StudentNotFoundException.class, () -> {
            enrollmentService.validateCreditLimit("S999", 20);
        });
    }

    @Test
    @DisplayName("SOAL 1B (Stub): Drop Mata Kuliah Berhasil")
    void testDropCourseSuccess_AsStub() {
        // 1. Arrange (Persiapan STUB)
        when(studentRepository.findById("S001")).thenReturn(fakeStudent);
        when(courseRepository.findByCourseCode("CS-101")).thenReturn(fakeCourse);

        // 2. Act & 3. Assert
        // Tes ini lolos jika tidak ada error (void method)
        assertDoesNotThrow(() -> {
            enrollmentService.dropCourse("S001", "CS-101");
        });
    }

    // --- SOAL 1C: UNIT TEST MENGGUNAKAN MOCK ---
    //
    // MOCK: Fokus pada VERIFIKASI PERILAKU (Behavior Verification).
    // Kita tidak hanya peduli pada *hasil akhir*, tapi kita ingin MEMASTIKAN
    // bahwa 'EnrollmentService' MEMANGGIL method lain dengan benar.
    // Kita menggunakan 'verify()' untuk "memata-matai" objek palsu (Mock).
    //
    // Di soal ini, method 'enrollCourse' diuji sebagai Mock.
    //

    @Test
    @DisplayName("SOAL 1C (Mock): Pendaftaran Matkul Berhasil (Happy Path)")
    void testEnrollCourseSuccess_AsMock() {
        // 1. Arrange (Persiapan data stub)
        // Kita masih butuh stub untuk data palsu
        when(studentRepository.findById("S001")).thenReturn(fakeStudent);
        when(courseRepository.findByCourseCode("CS-101")).thenReturn(fakeCourse);
        when(courseRepository.isPrerequisiteMet("S001", "CS-101")).thenReturn(true);

        // 2. Act
        Enrollment result = enrollmentService.enrollCourse("S001", "CS-101");

        // 3. Assert (Validasi Hasil - Bagian dari Stub)
        assertNotNull(result);
        assertEquals("APPROVED", result.getStatus());

        // 4. Verify (Validasi Perilaku - INTI MOCK)

        // VERIFIKASI bahwa 'courseRepository.update()'
        // dipanggil TEPAT 1 KALI.
        verify(courseRepository, times(1)).update(fakeCourse);

        // VERIFIKASI bahwa 'notificationService.sendEmail()'
        // dipanggil TEPAT 1 KALI dengan email dan subject yang TEPAT.
        verify(notificationService, times(1)).sendEmail(
                eq("email@test.com"), // eq() = "equals"
                eq("Enrollment Confirmation"),
                contains("You have been enrolled") // contains() = berisi teks
        );
    }

    @Test
    @DisplayName("SOAL 1C (Mock): Pendaftaran Gagal (Mahasiswa Suspended)")
    void testEnrollCourseFailedStudentSuspended_AsMock() {
        // 1. Arrange
        fakeStudent.setAcademicStatus("SUSPENDED");
        when(studentRepository.findById("S001")).thenReturn(fakeStudent);

        // 2. Act & 3. Assert
        assertThrows(EnrollmentException.class, () -> {
            enrollmentService.enrollCourse("S001", "CS-101");
        });

        // 4. Verify (Validasi Perilaku - INTI MOCK)

        // VERIFIKASI bahwa 'courseRepository' TIDAK PERNAH memanggil 'update'
        verify(courseRepository, never()).update(any(Course.class));

        // VERIFIKASI bahwa 'notificationService' TIDAK PERNAH mengirim email
        verify(notificationService, never()).sendEmail(anyString(), anyString(), anyString());
    }

    // (Tes skenario error lainnya seperti CourseFull, PrerequisiteNotMet, dll.
    // juga termasuk dalam pengujian MOCK, karena kita mem-verifikasi
    // bahwa 'update' dan 'sendEmail' TIDAK PERNAH dipanggil)
}