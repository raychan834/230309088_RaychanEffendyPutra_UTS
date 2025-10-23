package com.siakad.model;

// Impor anotasi JUnit
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
// Impor kelas-kelas Java standar
import java.util.ArrayList;
import java.util.List;
// Impor method 'static' assertions
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tes ini untuk menguji model Course.java.
 * Kita akan menguji kedua constructor, semua getter/setter,
 * dan method 'addPrerequisite' untuk mencapai 100% coverage.
 */
class CourseTest {

    @Test
    @DisplayName("Test Constructor Kosong, Getter, dan Setter")
    void testGettersAndSetters() {
        // 1. Arrange (Persiapan) & 2. Act (Eksekusi)

        // Panggil constructor kosong
        Course course = new Course();

        // Siapkan data list prerequisite
        List<String> prereqs = new ArrayList<>();
        prereqs.add("PR-100");

        // Panggil semua setter untuk mengisi data
        course.setCourseCode("CS-101");
        course.setCourseName("Dasar Pemrograman");
        course.setCredits(4);
        course.setCapacity(50);
        course.setEnrolledCount(45);
        course.setLecturer("Dr. Budi");
        course.setPrerequisites(prereqs); // Set list prerequisite

        // 3. Assert (Validasi)
        // Verifikasi semua getter mengembalikan data yang benar
        assertAll("Verifikasi getter dan setter",
                () -> assertEquals("CS-101", course.getCourseCode()),
                () -> assertEquals("Dasar Pemrograman", course.getCourseName()),
                () -> assertEquals(4, course.getCredits()),
                () -> assertEquals(50, course.getCapacity()),
                () -> assertEquals(45, course.getEnrolledCount()),
                () -> assertEquals("Dr. Budi", course.getLecturer()),
                () -> assertEquals(prereqs, course.getPrerequisites())
        );
    }

    @Test
    @DisplayName("Test Constructor Lengkap (All-Args)")
    void testAllArgsConstructor() {
        // 1. Arrange (Persiapan) & 2. Act (Eksekusi)
        // Panggil constructor lengkap
        Course course = new Course(
                "CS-201",
                "Struktur Data",
                3,
                40,
                0,
                "Dr. Ani"
        );

        // 3. Assert (Validasi)
        // Cek semua field yang di-set oleh constructor
        assertEquals("CS-201", course.getCourseCode());
        assertEquals("Struktur Data", course.getCourseName());
        assertEquals(3, course.getCredits());
        assertEquals(40, course.getCapacity());
        assertEquals(0, course.getEnrolledCount());
        assertEquals("Dr. Ani", course.getLecturer());
        // Pastikan list prerequisite terbuat (meskipun kosong)
        assertNotNull(course.getPrerequisites());
    }

    @Test
    @DisplayName("Test method addPrerequisite (FIXED)")
    void testAddPrerequisite() {
        // 1. Arrange
        // Buat objek menggunakan constructor kosong
        Course course = new Course();

        // --- INI BAGIAN YANG DIPERBAIKI ---
        // Kode 'Course.java' langsung membuat ArrayList kosong.
        // Jadi, kita tes apakah list-nya 'tidak null' dan 'kosong'.
        assertNotNull(course.getPrerequisites(), "List seharusnya tidak null setelah constructor");
        assertTrue(course.getPrerequisites().isEmpty(), "List seharusnya kosong setelah constructor");
        // --- Selesai Perbaikan ---

        // 2. Act
        // Panggil method addPrerequisite
        // Ini harusnya otomatis membuat ArrayList baru
        course.addPrerequisite("CS-101");

        // 3. Assert
        assertNotNull(course.getPrerequisites()); // Pastikan list masih ada
        assertEquals(1, course.getPrerequisites().size()); // Pastikan isinya 1
        assertEquals("CS-101", course.getPrerequisites().get(0)); // Cek isinya

        // Tes tambah lagi untuk memastikan list-nya berfungsi normal
        course.addPrerequisite("CS-102");
        assertEquals(2, course.getPrerequisites().size());
        assertEquals("CS-102", course.getPrerequisites().get(1));
    }
    @Test
    @DisplayName("Test addPrerequisite saat list awalnya null (untuk 100% coverage)")
    void testAddPrerequisiteWhenListIsNull() {
        // 1. Arrange (Persiapan)
        // Buat objek course baru
        Course course = new Course();

        // SECARA MANUAL paksa list prerequisites menjadi null
        // Ini adalah langkah kunci untuk menguji branch yang terlewat
        course.setPrerequisites(null);

        // Pastikan list-nya benar-benar null sebelum tes
        assertNull(course.getPrerequisites(), "List seharusnya null sebelum dipanggil");

        // 2. Act (Eksekusi)
        // Panggil method addPrerequisite.
        // Ini akan memicu 'if (this.prerequisites == null)'
        // dan mengeksekusi baris yang tadinya MERAH
        course.addPrerequisite("PR-101");

        // 3. Assert (Validasi)
        // Pastikan list-nya tidak lagi null (sudah dibuatkan ArrayList baru)
        assertNotNull(course.getPrerequisites(), "List seharusnya sudah dibuat");
        // Pastikan data berhasil ditambahkan
        assertEquals(1, course.getPrerequisites().size());
        assertEquals("PR-101", course.getPrerequisites().get(0));
    }
}