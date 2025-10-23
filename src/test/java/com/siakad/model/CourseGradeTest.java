package com.siakad.model;

// Impor anotasi JUnit
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

// Impor method 'static' assertions
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tes ini untuk melengkapi coverage CourseGrade.java
 * Kita akan menguji method-method yang sebelumnya MERAH di laporan JaCoCo:
 * - CourseGrade() (constructor kosong)
 * - setCourseCode()
 * - setCredits()
 * - setGradePoint()
 * - getCourseCode()
 */
class CourseGradeTest {

    @Test
    @DisplayName("Test Constructor Kosong, Getter, dan Setter")
    void testGettersAndSetters() {
        // 1. Arrange (Persiapan) & 2. Act (Eksekusi)

        // Panggil constructor kosong
        // Ini akan meng-cover 'CourseGrade()' yang masih 0%
        CourseGrade grade = new CourseGrade();

        // Panggil semua setter
        // Ini akan meng-cover 'setCourseCode', 'setCredits', 'setGradePoint'
        grade.setCourseCode("MK-404");
        grade.setCredits(4);
        grade.setGradePoint(3.5); // Nilai B+

        // 3. Assert (Validasi)
        // Kita cek semua getter untuk memastikan nilainya tersimpan
        assertAll("Verifikasi semua getter dan setter",

                // Panggil 'getCourseCode()'
                // Ini akan meng-cover 'getCourseCode' yang masih 0%
                () -> assertEquals("MK-404", grade.getCourseCode()),

                // Getter 'getCredits' dan 'getGradePoint' sudah hijau,
                // tapi tetap kita panggil untuk memastikan setter-nya berfungsi
                () -> assertEquals(4, grade.getCredits()),
                () -> assertEquals(3.5, grade.getGradePoint())
        );
    }

    @Test
    @DisplayName("Test Constructor Lengkap (All-Args)")
    void testAllArgsConstructor() {
        // 1. Arrange (Persiapan) & 2. Act (Eksekusi)

        // Panggil constructor lengkap
        // (Ini yang sudah 100% di laporan JaCoCo,
        //  tapi baik untuk dibuatkan tes terpisahnya)
        CourseGrade grade = new CourseGrade("MK-101", 3, 4.0);

        // 3. Assert (Validasi)
        // Kita tes lagi untuk kelengkapan
        assertEquals("MK-101", grade.getCourseCode());
        assertEquals(3, grade.getCredits());
        assertEquals(4.0, grade.getGradePoint());
    }
}