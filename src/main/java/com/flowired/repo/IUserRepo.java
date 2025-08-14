package com.flowired.repo;

import com.flowired.model.User;

public interface IUserRepo extends IGenericRepo<User, Integer>{

    //@Query("FROM User u WHERE u.username = :username")
    //Derived Query
    User findOneByUsername(String username);

}
