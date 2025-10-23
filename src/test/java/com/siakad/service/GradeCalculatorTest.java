package com.siakad.service;

// Mengimpor kelas-kelas yang kita butuhkan
import com.siakad.model.CourseGrade; // Model data untuk nilai matkul
import org.junit.jupiter.api.BeforeEach; // Anotasi untuk method setup
import org.junit.jupiter.api.DisplayName; // Memberi nama tes yang mudah dibaca
import org.junit.jupiter.api.Test; // Anotasi untuk menandakan sebuah method adalah tes
import org.junit.jupiter.params.ParameterizedTest; // Anotasi untuk tes dengan banyak parameter
import org.junit.jupiter.params.provider.CsvSource; // Sumber data parameter dari CSV (teks)

import java.util.ArrayList;
import java.util.List;

// Mengimpor semua method 'static' dari Assertions agar bisa dipakai langsung
// seperti assertEquals(), assertThrows(), dll.
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit Test untuk GradeCalculator.
 * Sesuai Soal 1A: Unit Test Biasa (tanpa mock/stub)
 */
class GradeCalculatorTest {

    // Deklarasikan objek yang akan diuji
    private GradeCalculator calculator;

    /**
     * Method 'setUp' ini akan berjalan SEBELUM setiap method @Test dieksekusi.
     * Tujuannya adalah memastikan setiap tes mendapatkan objek 'calculator' baru
     * sehingga tes satu tidak memengaruhi tes lainnya.
     */
    @BeforeEach
    void setUp() {
        // Inisialisasi objek GradeCalculator baru
        calculator = new GradeCalculator();
    }

    // --- Pengujian untuk method calculateGPA() ---

    /**
     * Tes 'Happy Path' atau skenario normal.
     * Menguji perhitungan IPK dengan data yang valid.
     */
    @Test
    @DisplayName("Skenario Normal: Menghitung IPK dari beberapa matkul")
    void testCalculateGpaNormal() {
        // 1. Arrange (Persiapan)
        // Kita siapkan data input, yaitu daftar nilai (List<CourseGrade>)
        List<CourseGrade> grades = new ArrayList<>();
        // Matkul 1: 3 SKS, Nilai A (4.0)
        grades.add(new CourseGrade("MK-101", 3, 4.0));
        // Matkul 2: 3 SKS, Nilai C (2.0)
        grades.add(new CourseGrade("MK-102", 3, 2.0));

        // Perhitungan manual: ( (3 * 4.0) + (3 * 2.0) ) / (3 + 3) = 18.0 / 6 = 3.0
        double expectedGpa = 3.0; // Ini adalah hasil yang kita harapkan

        // 2. Act (Eksekusi)
        // Kita panggil method yang mau diuji dengan data dari 'Arrange'
        double actualGpa = calculator.calculateGPA(grades);

        // 3. Assert (Validasi)
        // Kita bandingkan hasil harapan (expected) dengan hasil sebenarnya (actual)
        assertEquals(expectedGpa, actualGpa, "Perhitungan IPK harusnya 3.0");
    }

    /**
     * Tes 'Edge Case' (kasus di ujung).
     * Menguji jika list-nya kosong. Ini penting untuk 'branch coverage' di kode aslinya.
     * `if (grades == null || grades.isEmpty()) { ... }`
     */
    @Test
    @DisplayName("Skenario List Kosong: IPK harus 0.0")
    void testCalculateGpaEmptyList() {
        // 1. Arrange
        List<CourseGrade> grades = new ArrayList<>(); // List kosong

        // 2. Act
        double actualGpa = calculator.calculateGPA(grades);

        // 3. Assert
        // Jika list kosong, method harus mengembalikan 0.0
        assertEquals(0.0, actualGpa, "IPK list kosong harus 0.0");
    }

    /**
     * Tes 'Edge Case' lainnya.
     * Menguji jika inputnya 'null'. Ini menguji branch `if (grades == null ...)`
     */
    @Test
    @DisplayName("Skenario List Null: IPK harus 0.0")
    void testCalculateGpaNullList() {
        // 1. Arrange (input-nya null)
        // 2. Act
        double actualGpa = calculator.calculateGPA(null); // Langsung masukkan null
        // 3. Assert
        assertEquals(0.0, actualGpa, "IPK list null harus 0.0");
    }

    /**
     * Tes 'Edge Case' lainnya.
     * Menguji jika total SKS adalah 0. Ini menguji branch `if (totalCredits == 0)`
     */
    @Test
    @DisplayName("Skenario Total SKS 0: IPK harus 0.0")
    void testCalculateGpaZeroCredits() {
        // 1. Arrange
        List<CourseGrade> grades = new ArrayList<>();
        grades.add(new CourseGrade("MK-000", 0, 4.0)); // Matkul dengan 0 SKS

        // 2. Act
        double actualGpa = calculator.calculateGPA(grades);

        // 3. Assert
        assertEquals(0.0, actualGpa, "IPK dengan total SKS 0 harus 0.0");
    }

    /**
     * Tes Skenario Error (Exception).
     * Menguji jika ada grade point < 0. Ini menguji branch validasi di dalam loop.
     * `if (grade.getGradePoint() < 0 || ...)`
     */
    @Test
    @DisplayName("Skenario Error: Grade point tidak valid (< 0)")
    void testCalculateGpaInvalidGradePointNegative() {
        // 1. Arrange
        List<CourseGrade> grades = new ArrayList<>();
        grades.add(new CourseGrade("MK-999", 3, -1.0)); // Nilai -1.0 (tidak valid)

        // 2. Act & 3. Assert
        // Kita gunakan 'assertThrows' untuk memastikan bahwa kode di dalamnya
        // PASTI akan melempar (throw) sebuah IllegalArgumentException.
        // Jika tidak melempar exception, tes ini akan GAGAL.
        assertThrows(IllegalArgumentException.class, () -> {
            // Ini adalah kode yang kita harapkan akan melempar error
            calculator.calculateGPA(grades);
        }, "Harus melempar error jika grade point negatif");
    }

    /**
     * Tes Skenario Error (Exception).
     * Menguji jika ada grade point > 4.0.
     * `if (... || grade.getGradePoint() > 4.0)`
     */
    @Test
    @DisplayName("Skenario Error: Grade point tidak valid (> 4.0)")
    void testCalculateGpaInvalidGradePointPositive() {
        // 1. Arrange
        List<CourseGrade> grades = new ArrayList<>();
        grades.add(new CourseGrade("MK-888", 3, 5.0)); // Nilai 5.0 (tidak valid)

        // 2. Act & 3. Assert
        assertThrows(IllegalArgumentException.class, () -> {
            calculator.calculateGPA(grades);
        }, "Harus melempar error jika grade point > 4.0");
    }

    // --- Pengujian untuk method determineAcademicStatus() ---

    /**
     * Ini adalah @ParameterizedTest.
     * Daripada menulis 10 method @Test yang berbeda, kita bisa satukan di sini.
     * Method ini akan dijalankan berulang kali untuk setiap baris di @CsvSource.
     * Ini sangat efisien untuk menguji SEMUA branch (if/else) di method status.
     */
    @ParameterizedTest(name = "IPK {0}, Sem {1} -> Status {2}") // Format nama tes
    @CsvSource({
            // Data CSV: "gpa, semester, expectedStatus"
            // Semester 1-2
            "2.5, 1, ACTIVE",    // Tes branch: semester <= 2, gpa >= 2.0
            "2.0, 2, ACTIVE",    // Tes batas bawah
            "1.99, 2, PROBATION", // Tes branch: semester <= 2, gpa < 2.0
            // Semester 3-4
            "3.0, 3, ACTIVE",    // Tes branch: semester <= 4, gpa >= 2.25
            "2.25, 4, ACTIVE",   // Tes batas bawah
            "2.24, 3, PROBATION", // Tes branch: semester <= 4, gpa >= 2.0
            "2.0, 4, PROBATION", // Tes batas bawah
            "1.99, 3, SUSPENDED", // Tes branch: semester <= 4, gpa < 2.0
            // Semester 5+
            "4.0, 5, ACTIVE",    // Tes branch: semester > 4, gpa >= 2.5
            "2.5, 8, ACTIVE",    // Tes batas bawah
            "2.49, 5, PROBATION", // Tes branch: semester > 4, gpa >= 2.0
            "2.0, 6, PROBATION", // Tes batas bawah
            "1.99, 7, SUSPENDED"  // Tes branch: semester > 4, gpa < 2.0
    })
    @DisplayName("Test semua skenario status akademik")
    void testDetermineAcademicStatus(double gpa, int semester, String expectedStatus) {
        // 1. Arrange (Sudah dilakukan oleh parameter)

        // 2. Act
        String actualStatus = calculator.determineAcademicStatus(gpa, semester);

        // 3. Assert
        assertEquals(expectedStatus, actualStatus);
    }

    /**
     * Tes Skenario Error (Exception).
     * Menguji validasi input IPK.
     * `if (gpa < 0 || gpa > 4.0)`
     */
    @Test
    @DisplayName("Status Error: IPK negatif")
    void testDetermineStatusErrorInvalidGpa() {
        assertThrows(IllegalArgumentException.class, () -> {
            calculator.determineAcademicStatus(-0.1, 3); // IPK -0.1 (tidak valid)
        }, "Harus melempar error jika IPK negatif");
    }

    /**
     * Tes Skenario Error (Exception).
     * Menguji validasi input semester.
     * `if (semester < 1)`
     */
    @Test
    @DisplayName("Status Error: Semester 0 atau negatif")
    void testDetermineStatusErrorInvalidSemester() {
        assertThrows(IllegalArgumentException.class, () -> {
            calculator.determineAcademicStatus(3.0, 0); // Semester 0 (tidak valid)
        }, "Harus melempar error jika semester < 1");
    }

    // --- Pengujian untuk method calculateMaxCredits() ---

    /**
     * Menggunakan @ParameterizedTest lagi untuk menguji semua branch batas SKS.
     * Ini sangat efisien.
     */
    @ParameterizedTest(name = "IPK {0} -> Maks {1} SKS")
    @CsvSource({
            // Data CSV: "gpa, expectedCredits"
            "4.0, 24",  // Tes branch: gpa >= 3.0
            "3.0, 24",  // Tes batas
            "2.99, 21", // Tes branch: gpa >= 2.5
            "2.5, 21",  // Tes batas
            "2.49, 18", // Tes branch: gpa >= 2.0
            "2.0, 18",  // Tes batas
            "1.99, 15", // Tes branch: gpa < 2.0
            "0.0, 15"   // Tes batas
    })
    @DisplayName("Test semua skenario batas SKS")
    void testCalculateMaxCredits(double gpa, int expectedCredits) {
        // 1. Arrange (dari parameter)

        // 2. Act
        int actualCredits = calculator.calculateMaxCredits(gpa);

        // 3. Assert
        assertEquals(expectedCredits, actualCredits);
    }

    /**
     * Tes Skenario Error (Exception).
     * Menguji validasi input IPK.
     * `if (gpa < 0 || gpa > 4.0)`
     */
    @Test
    @DisplayName("Kredit Error: IPK lebih dari 4.0")
    void testCalculateMaxCreditsError() {
        assertThrows(IllegalArgumentException.class, () -> {
            calculator.calculateMaxCredits(4.1); // IPK 4.1 (tidak valid)
        }, "Harus melempar error jika IPK > 4.0");
    }
}