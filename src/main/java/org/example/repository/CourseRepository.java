package org.example.repository;

import org.example.model.Course;
import org.hibernate.SessionFactory;

public class CourseRepository extends GenericRepositoryImpl<Course, Long> {

    public CourseRepository(SessionFactory sessionFactory, Class<Course> type) {
        super(sessionFactory, type);
    }
}

