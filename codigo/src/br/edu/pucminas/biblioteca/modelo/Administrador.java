package br.edu.pucminas.biblioteca.modelo;

import java.lang.reflect.Field;

public class Administrador extends Usuario {
    
    
    public Administrador(String nome, String senha) {
        super(nome, senha);
    }

    /**Nota de transparência sobre uso de IA: A classe Administrador não poderia modificar a senha de outros usuários, pois ela não tem acesso aos atributos de outros objetos,
     a ferramenta Claude da anthropic sugeriu criar uma função Reflection para fazer o acesso e modificação da senha dos outros usuários. Essa decisão foi tomada para que o diagrama
     permanecesse da mesma forma.
     */

    public void redefinirSenha(Usuario usuario, String novaSenha) {
        if (usuario == null)
            throw new IllegalArgumentException("Usuário não pode ser nulo");
        if (novaSenha == null || novaSenha.isBlank())
            throw new IllegalArgumentException("Nova senha não pode ser vazia");

        try {
            Field campoSenha = Usuario.class.getDeclaredField("senha");
            campoSenha.setAccessible(true);
            campoSenha.set(usuario, novaSenha);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new IllegalStateException("Não foi possível redefinir a senha do usuário", e);
        }
    }

}
