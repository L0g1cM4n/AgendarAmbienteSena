package com.sena.database_connection.model.dto;

import com.sena.database_connection.model.entities.Usuario;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioResponseDTO {

    private Long id;
    private String documento;
    private String nombreCompleto;
    private String correo;
    private Boolean activo;

    public static UsuarioResponseDTO fromEntity(Usuario u) {
        return new UsuarioResponseDTO(
            u.getId(),
            u.getDocumento(),
            u.getNombreCompleto(),
            u.getCorreo(),
            u.getActivo()
        );
    }
}
