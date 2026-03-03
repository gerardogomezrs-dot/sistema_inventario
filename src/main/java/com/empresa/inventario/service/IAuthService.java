package com.empresa.inventario.service;

import com.empresa.inventario.model.Usuario;

public interface IAuthService {

	Usuario login(String userName, String password);
}
