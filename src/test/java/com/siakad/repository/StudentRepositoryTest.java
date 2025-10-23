package com.siakad.repository;

// Impor anotasi JUnit
import com.siakad.model.Student;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

// Impor method 'static' assertions
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tes ini untuk menguji model StudentRepository.java.
 *
 * PENTING: Sama seperti CourseRepositoryTest,
 * tes ini hanya bertujuan untuk MEMANGGIL setiap method
 * agar mendapatkan 100% Code Coverage di laporan JaCoCo,
 * karena class aslinya berisi method-method kosong.
 */
class StudentRepositoryTest {

    // Deklarasikan objek yang akan diuji
    private StudentRepository studentRepository;

    /**
     * Method 'setUp' ini berjalan sebelum setiap tes.
     * Kita inisialisasi objeknya di sini.
     */
    @BeforeEach
    void setUp() {
        // Buat instance baru dari class-nya
        studentRepository = new StudentRepository();
    }

    @Test
    @DisplayName("Test method findById (untuk coverage)")
    void testFindById() {
        // 1. Arrange & 2. Act
        // Kita panggil method-nya. Sesuai kode aslinya, ini akan
        // mengembalikan 'null'.
        Object result = studentRepository.findById("S001");

        // 3. Assert
        // Kita pastikan hasilnya 'null', sesuai dengan isi method-nya
        assertNull(result, "Method findById harus mengembalikan null");
    }

    @Test
    @DisplayName("Test method update (untuk coverage)")
    void testUpdate() {
        // 1. Arrange & 2. Act
        // Method 'update' aslinya adalah 'void' (kosong).
        // Kita panggil saja untuk mendapatkan coverage.

        // 3. Assert
        // Tes ini akan lolos jika pemanggilan di bawah ini
        // tidak melempar error.
        assertDoesNotThrow(() -> {
            studentRepository.update(null); // 'null' tidak masalah, method-nya kosong
        }, "Method update seharusnya tidak melempar error");
    }

    @Test
    @DisplayName("Test method getCompletedCourses (untuk coverage)")
    void testGetCompletedCourses() {
        // 1. Arrange & 2. Act
        // Kita panggil method-nya. Sesuai kode aslinya, ini akan
        // mengembalikan 'null'.
        Object result = studentRepository.getCompletedCourses("S001");

        // 3. Assert
        // Kita pastikan hasilnya 'null', sesuai dengan isi method-nya
        assertNull(result, "Method getCompletedCourses harus mengembalikan null");
    }

    /**
     * Tes ini untuk menguji model Student.java.
     * Kita akan menguji kedua constructor (kosong dan lengkap)
     * dan semua method getter/setter untuk 100% coverage.
     */
    static class StudentTest {

        @Test
        @DisplayName("Test Constructor Kosong, Getter, dan Setter")
        void testGettersAndSetters() {
            // 1. Arrange (Persiapan) & 2. Act (Eksekusi)

            // Panggil constructor kosong
            Student student = new Student();

            // Panggil semua method 'set' untuk mengisi data
            student.setStudentId("S001");
            student.setName("Raychan");
            student.setEmail("raychan@email.com");
            student.setMajor("RKS");
            student.setSemester(5);
            student.setGpa(3.9);
            student.setAcademicStatus("ACTIVE");

            // 3. Assert (Validasi)

            // 'assertAll' digunakan untuk menjalankan semua assertions di dalamnya
            // dan melaporkan semua yang gagal, tidak hanya berhenti di yang pertama.
            // Kita cek menggunakan 'get' apakah data yang kita 'set' tadi
            // tersimpan dengan benar.
            assertAll("Verifikasi semua getter setelah setter dipanggil",
                    () -> assertEquals("S001", student.getStudentId()),
                    () -> assertEquals("Raychan", student.getName()),
                    () -> assertEquals("raychan@email.com", student.getEmail()),
                    () -> assertEquals("RKS", student.getMajor()),
                    () -> assertEquals(5, student.getSemester()),
                    () -> assertEquals(3.9, student.getGpa()),
                    () -> assertEquals("ACTIVE", student.getAcademicStatus())
            );
        }

        @Test
        @DisplayName("Test Constructor Lengkap (All-Args Constructor)")
        void testAllArgsConstructor() {
            // 1. Arrange (Persiapan) & 2. Act (Eksekusi)

            // Panggil constructor lengkap dengan semua data
            Student student = new Student(
                    "S002",
                    "Budi",
                    "budi@email.com",
                    "Siber",
                    3,
                    3.1,
                    "PROBATION"
            );

            // 3. Assert (Validasi)

            // Kita cek lagi semua 'get' untuk memastikan
            // constructor-nya mengisi semua field dengan benar.
            assertAll("Verifikasi semua field setelah constructor lengkap dipanggil",
                    () -> assertEquals("S002", student.getStudentId()),
                    () -> assertEquals("Budi", student.getName()),
                    () -> assertEquals("budi@email.com", student.getEmail()),
                    () -> assertEquals("Siber", student.getMajor()),
                    () -> assertEquals(3, student.getSemester()),
                    () -> assertEquals(3.1, student.getGpa()),
                    () -> assertEquals("PROBATION", student.getAcademicStatus())
            );
        }
    }
}