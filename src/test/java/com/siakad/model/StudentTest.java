package com.siakad.model;

// Impor anotasi JUnit
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

// Impor method 'static' assertions
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tes ini untuk menguji model Student.java.
 * Kita akan menguji kedua constructor (kosong dan lengkap)
 * dan semua method getter/setter untuk 100% coverage.
 */
class StudentTest {

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