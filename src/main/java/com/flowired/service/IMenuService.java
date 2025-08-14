package com.flowired.service;

import com.flowired.model.Menu;

import java.util.List;

public interface IMenuService extends ICRUD<Menu, Integer>{

    List<Menu> getMenusByUsername(String username);
}
