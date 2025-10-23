package com.siakad.repository;

import com.siakad.model.Course;
import com.siakad.model.Student;

import java.util.List;

/**
 * Interface untuk akses data mahasiswa
 * Interface ini akan di-stub atau di-mock dalam unit testing
 */

public class StudentRepository {

    /**
     * Mencari mahasiswa berdasarkan student ID
     *
     * @param studentId ID mahasiswa
     * @return Student object atau null jika tidak ditemukan
     */
    public Student findById(String studentId) {
        return null;
    }

    /**
     * Update data mahasiswa
     *
     * @param student Student object yang akan diupdate
     */
    void update(Student student) {

    }

    /**
     * Mendapatkan daftar mata kuliah yang sudah diselesaikan mahasiswa
     *
     * @param studentId ID mahasiswa
     * @return List of Course yang sudah diselesaikan
     */
    List<Course> getCompletedCourses(String studentId) {
        return null;
    }
}