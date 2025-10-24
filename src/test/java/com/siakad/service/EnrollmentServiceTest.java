package com.siakad.service;

// Impor kelas-kelas utama
import com.siakad.exception.*;
import com.siakad.model.Course;
import com.siakad.model.Enrollment;
import com.siakad.model.Student;
import com.siakad.repository.CourseRepository;
import com.siakad.repository.StudentRepository;

// Impor anotasi JUnit dan Mockito
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

// Impor method 'static' dari Mockito untuk stubbing (when) dan verification (verify)
import static org.mockito.Mockito.*;
// Impor method 'static' dari Assertions
import static org.junit.jupiter.api.Assertions.*;

/**
 * Menggunakan MockitoExtension untuk mengaktifkan anotasi @Mock dan @InjectMocks
 */
@ExtendWith(MockitoExtension.class)
class EnrollmentServiceTest {


    // --- BAGIAN 1: SETUP MOCKITO ---

    /**
     * @Mock: Membuat objek palsu (mock) dari class/interface ini.
     * Kita akan mengontrol penuh perilaku dari dependensi-dependensi ini.
     */
    @Mock
    private StudentRepository studentRepository; // Dependensi palsu

    @Mock
    private CourseRepository courseRepository; // Dependensi palsu

    @Mock
    private NotificationService notificationService; // Dependensi palsu

    /**
     * @Mock: Kita juga bisa me-mock GradeCalculator,
     * tapi karena kita sudah punya GradeCalculatorTest yang 100%,
     * lebih baik kita pakai yang asli (bukan mock).
     */
    private GradeCalculator gradeCalculator; // Dependensi ASLI

    /**
     * @InjectMocks: Membuat instance ASLI dari EnrollmentService
     * dan secara otomatis "menyuntikkan" (inject) semua objek
     * yang ditandai @Mock (studentRepository, courseRepository, notificationService)
     * ke dalamnya.
     */
    @InjectMocks
    private EnrollmentService enrollmentService;

    // Data palsu untuk digunakan di berbagai tes
    private Student fakeStudent;
    private Course fakeCourse;

    /**
     * Method 'setUp' ini berjalan sebelum setiap tes.
     * Kita inisialisasi GradeCalculator asli dan data palsu di sini.
     */
    @BeforeEach
    void setUp() {
        // Kita pakai GradeCalculator asli, bukan mock
        gradeCalculator = new GradeCalculator();

        // Inisialisasi ulang enrollmentService dengan mock DAN GradeCalculator asli
        // @InjectMocks terkadang tidak bisa menangani constructor yang campur
        // antara objek asli dan mock, jadi kita buat manual untuk memastikan.
        enrollmentService = new EnrollmentService(
                studentRepository,
                courseRepository,
                notificationService,
                gradeCalculator // Suntik GradeCalculator asli
        );

        // Siapkan data palsu yang umum dipakai
        fakeStudent = new Student(
                "S001", "Nama Mahasiswa", "mahasiswa@email.com", "RKS", 5, 3.5, "ACTIVE"
        );
        fakeCourse = new Course(
                "CS-101", "Dasar Pemrograman", 3, 50, 49, "Dr. Budi"
        );
    }

    // --- BAGIAN 2: SOAL 1B - UNIT TEST MENGGUNAKAN STUB ---
    // "Stub" berarti kita fokus pada PENGATURAN DATA (State Verification).
    // Kita mengatur 'when().thenReturn()' untuk menyediakan data palsu
    // dan memeriksa hasil (return value) dari method yang diuji.

    @Test
    @DisplayName("SOAL 1B (Stub): Validasi SKS Diterima (IPK 3.5, Minta 24 SKS)")
    void testValidateCreditLimitSuccess() {
        // 1. Arrange (Persiapan STUB)
        // Kita atur stub 'studentRepository':
        // "KETIKA (when) method findById dipanggil dengan ID 'S001',
        //  MAKA KEMBALIKAN (thenReturn) objek fakeStudent."
        when(studentRepository.findById("S001")).thenReturn(fakeStudent);

        // 2. Act (Eksekusi)
        // Panggil method yang diuji.
        // fakeStudent punya IPK 3.5, aturan bisnis -> maks 24 SKS.
        boolean result = enrollmentService.validateCreditLimit("S001", 24);

        // 3. Assert (Validasi)
        // Pastikan hasilnya 'true' (validasi sukses)
        assertTrue(result, "IPK 3.5 harusnya boleh ambil 24 SKS");
    }

    @Test
    @DisplayName("SOAL 1B (Stub): Validasi SKS Ditolak (IPK 2.1, Minta 21 SKS)")
    void testValidateCreditLimitFailed() {
        // 1. Arrange (Persiapan STUB)
        // Ubah IPK di data palsu kita
        fakeStudent.setGpa(2.1);
        // Atur stub untuk mengembalikan student dengan IPK 2.1
        when(studentRepository.findById("S001")).thenReturn(fakeStudent);

        // 2. Act
        // Panggil method yang diuji.
        // Aturan bisnis: IPK 2.1 -> maks 18 SKS. Kita minta 21.
        boolean result = enrollmentService.validateCreditLimit("S001", 21);

        // 3. Assert
        // Pastikan hasilnya 'false' (validasi gagal)
        // Ini akan meng-cover branch 'false' dari 'return' di line 104
        assertFalse(result, "IPK 2.1 tidak boleh ambil 21 SKS, maks 18");
    }

    @Test
    @DisplayName("SOAL 1B (Stub): Validasi SKS Error (Student Tidak Ditemukan)")
    void testValidateCreditLimitStudentNotFound() {
        // 1. Arrange (Persiapan STUB)
        // Atur stub 'studentRepository':
        // "KETIKA (when) method findById dipanggil dengan ID 'S999',
        //  MAKA KEMBALIKAN (thenReturn) null."
        when(studentRepository.findById("S999")).thenReturn(null);

        // 2. Act & 3. Assert
        // Pastikan method ini melempar 'StudentNotFoundException'
        // Ini akan meng-cover baris 101 yang MERAH
        assertThrows(StudentNotFoundException.class, () -> {
            enrollmentService.validateCreditLimit("S999", 20);
        }, "Harus melempar error jika student tidak ditemukan");
    }

    // --- Tambahan tes untuk 'dropCourse' (Soal 1B) ---

    @Test
    @DisplayName("SOAL 1B (Stub): Drop Mata Kuliah Berhasil")
    void testDropCourseSuccess() {
        // 1. Arrange
        // Atur stub untuk mengembalikan data valid
        when(studentRepository.findById("S001")).thenReturn(fakeStudent);
        when(courseRepository.findByCourseCode("CS-101")).thenReturn(fakeCourse);

        // 2. Act & 3. Assert
        // Method 'dropCourse' mengembalikan 'void' (tidak ada return value)
        // Tes dianggap SUKSES jika method berjalan sampai selesai
        // TANPA melempar exception apapun.
        // Ini akan meng-cover semua baris di 'dropCourse' (115-131)
        assertDoesNotThrow(() -> {
            enrollmentService.dropCourse("S001", "CS-101");
        }, "Drop course tidak boleh melempar error jika data valid");

        // Verifikasi tambahan (menggabungkan Mock):
        // Pastikan email notifikasi drop dikirim
        verify(notificationService, times(1)).sendEmail(
                eq("mahasiswa@email.com"),
                eq("Course Drop Confirmation"),
                contains("You have dropped")
        );
        // Pastikan 'update' dipanggil untuk mengurangi jumlah enrolled
        verify(courseRepository, times(1)).update(fakeCourse);
    }

    @Test
    @DisplayName("SOAL 1B (Stub): Drop Mata Kuliah Gagal (Student Tidak Ditemukan)")
    void testDropCourseStudentNotFound() {
        // 1. Arrange
        // Atur student stub untuk mengembalikan null
        when(studentRepository.findById("S999")).thenReturn(null);

        // 2. Act & 3. Assert
        // Kita harapkan 'StudentNotFoundException'
        // Ini akan meng-cover baris 118 yang MERAH
        assertThrows(StudentNotFoundException.class, () -> {
            enrollmentService.dropCourse("S999", "CS-101");
        }, "Harus error jika student untuk drop course tidak ada");
    }

    @Test
    @DisplayName("SOAL 1B (Stub): Drop Mata Kuliah Gagal (Course Tidak Ditemukan)")
    void testDropCourseCourseNotFound() {
        // 1. Arrange
        // Atur student stub untuk mengembalikan student valid
        when(studentRepository.findById("S001")).thenReturn(fakeStudent);
        // Atur course stub untuk mengembalikan null
        when(courseRepository.findByCourseCode("C999")).thenReturn(null);

        // 2. Act & 3. Assert
        // Kita harapkan 'CourseNotFoundException'
        // Ini akan meng-cover baris 123 yang MERAH
        assertThrows(CourseNotFoundException.class, () -> {
            enrollmentService.dropCourse("S001", "C999");
        }, "Harus error jika course untuk drop tidak ada");
    }


    // --- BAGIAN 3: SOAL 1C - UNIT TEST MENGGUNAKAN MOCK ---
    // "Mock" berarti kita fokus pada VERIFIKASI PERILAKU (Behavior Verification).
    // Kita tidak hanya mengecek return value, tapi juga mem-VERIFIKASI
    // bahwa method lain (seperti sendEmail, update) dipanggil dengan benar.

    @Test
    @DisplayName("SOAL 1C (Mock): Pendaftaran Matkul Berhasil (Happy Path)")
    void testEnrollCourseSuccess() {
        // 1. Arrange (Persiapan STUB untuk data)
        // Kita butuh stub data untuk melewati semua validasi
        // a. Mahasiswa ada dan aktif
        when(studentRepository.findById("S001")).thenReturn(fakeStudent);
        // b. Mata kuliah ada
        when(courseRepository.findByCourseCode("CS-101")).thenReturn(fakeCourse);
        // c. Kapasitas belum penuh (fakeCourse.enrolled = 49, capacity = 50)
        // d. Prasyarat terpenuhi
        when(courseRepository.isPrerequisiteMet("S001", "CS-101")).thenReturn(true);

        // 2. Act (Eksekusi)
        // Panggil method 'enrollCourse'
        // Ini akan meng-cover semua baris hijau di 'enrollCourse' (47-85)
        // dan juga 'generateEnrollmentId' (138-139)
        Enrollment result = enrollmentService.enrollCourse("S001", "CS-101");

        // 3. Assert (Validasi Hasil / State Verification)
        assertNotNull(result, "Hasil enrollment tidak boleh null");
        assertEquals("APPROVED", result.getStatus(), "Status harus APPROVED");

        // 4. Verify (Validasi Perilaku / Behavior Verification - INI INTI MOCK)

        // VERIFIKASI bahwa method 'courseRepository.update()'
        // dipanggil TEPAT 1 KALI dengan objek 'fakeCourse'.
        verify(courseRepository, times(1)).update(fakeCourse);

        // VERIFIKASI bahwa method 'notificationService.sendEmail()'
        // dipanggil TEPAT 1 KALI dengan email dan subject yang TEPAT.
        verify(notificationService, times(1)).sendEmail(
                eq("mahasiswa@email.com"), // eq() = "equals"
                eq("Enrollment Confirmation"),
                contains("You have been enrolled") // contains() = berisi teks
        );
    }

    @Test
    @DisplayName("SOAL 1C (Mock): Pendaftaran Gagal (Mahasiswa Tidak Ditemukan)")
    void testEnrollCourseFailedStudentNotFound() {
        // 1. Arrange
        // Atur stub untuk mengembalikan null
        when(studentRepository.findById("S999")).thenReturn(null);

        // 2. Act & 3. Assert
        // Pastikan melempar StudentNotFoundException
        // Ini akan meng-cover baris 51 yang MERAH
        assertThrows(StudentNotFoundException.class, () -> {
            enrollmentService.enrollCourse("S999", "CS-101");
        }, "Harus melempar error jika student tidak ditemukan");
    }

    @Test
    @DisplayName("SOAL 1C (Mock): Pendaftaran Gagal (Mahasiswa Suspended)")
    void testEnrollCourseFailedStudentSuspended() {
        // 1. Arrange
        // Atur student menjadi SUSPENDED
        fakeStudent.setAcademicStatus("SUSPENDED");
        // Atur stub untuk mengembalikan student suspended ini
        when(studentRepository.findById("S001")).thenReturn(fakeStudent);

        // 2. Act & 3. Assert
        // Pastikan melempar EnrollmentException
        // Ini akan meng-cover baris 56 yang MERAH
        assertThrows(EnrollmentException.class, () -> {
            enrollmentService.enrollCourse("S001", "CS-101");
        }, "Harus melempar error jika student suspended");
    }

    @Test
    @DisplayName("SOAL 1C (Mock): Pendaftaran Gagal (Mata Kuliah Tidak Ditemukan)")
    void testEnrollCourseFailedCourseNotFound() {
        // 1. Arrange
        // Atur student valid
        when(studentRepository.findById("S001")).thenReturn(fakeStudent);
        // Atur course stub untuk mengembalikan null
        when(courseRepository.findByCourseCode("C999")).thenReturn(null);

        // 2. Act & 3. Assert
        // Pastikan melempar CourseNotFoundException
        // Ini akan meng-cover baris 61 yang MERAH
        assertThrows(CourseNotFoundException.class, () -> {
            enrollmentService.enrollCourse("S001", "C999");
        }, "Harus melempar error jika mata kuliah tidak ditemukan");
    }

    @Test
    @DisplayName("SOAL 1C (Mock): Pendaftaran Gagal (Mata Kuliah Penuh)")
    void testEnrollCourseFailedCourseFull() {
        // 1. Arrange
        // Atur mata kuliah menjadi penuh (enrolled = 50, capacity = 50)
        fakeCourse.setEnrolledCount(50);

        // Atur stub-stub
        when(studentRepository.findById("S001")).thenReturn(fakeStudent);
        when(courseRepository.findByCourseCode("CS-101")).thenReturn(fakeCourse);

        // 2. Act & 3. Assert
        // Pastikan melempar CourseFullException
        // Ini akan meng-cover baris 66 yang MERAH
        assertThrows(CourseFullException.class, () -> {
            enrollmentService.enrollCourse("S001", "CS-101");
        }, "Harus melempar error jika mata kuliah penuh");
    }

    @Test
    @DisplayName("SOAL 1C (Mock): Pendaftaran Gagal (Prasyarat Tidak Terpenuhi)")
    void testEnrollCourseFailedPrerequisiteNotMet() {
        // 1. Arrange
        // Atur stub-stub
        when(studentRepository.findById("S001")).thenReturn(fakeStudent);
        when(courseRepository.findByCourseCode("CS-101")).thenReturn(fakeCourse);
        // Atur stub 'isPrerequisiteMet' untuk mengembalikan 'false'
        when(courseRepository.isPrerequisiteMet("S001", "CS-101")).thenReturn(false);

        // 2. Act & 3. Assert
        // Pastikan melempar PrerequisiteNotMetException
        // Ini akan meng-cover baris 71 yang MERAH
        assertThrows(PrerequisiteNotMetException.class, () -> {
            enrollmentService.enrollCourse("S001", "CS-101");
        }, "Harus melempar error jika prasyarat tidak terpenuhi");
    }
}