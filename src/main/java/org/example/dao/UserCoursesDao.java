package org.example.dao;

import org.example.model.UserCourses;
import org.hibernate.SessionFactory;

public class UserCoursesDao extends GenericDaoImpl<UserCourses, Long> {
    public UserCoursesDao(Class<UserCourses> type) {
        super( type);
    }
}

