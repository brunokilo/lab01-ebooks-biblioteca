package br.edu.pucminas.biblioteca.modelo;

public class Licenca {
    private final int limiteAcessosSimultaneos = 60;
    private int acessosAtivos;

    public Licenca (){
        this.acessosAtivos = 0;
    }
    
    public boolean temVagaDisponivel(Ebook ebook){
        // TODO: implementar na Sprint 3
        return false;
    } 
    
    public boolean licencaExpirada(){
        // TODO: implementar na Sprint 3
        return false;
    }

    public int getAcessosAtivos() {
        return acessosAtivos;
    }

    public void setAcessosAtivos(int acessosAtivos) {
        this.acessosAtivos = acessosAtivos;
    }
}
