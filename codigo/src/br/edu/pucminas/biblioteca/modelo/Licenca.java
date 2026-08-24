package br.edu.pucminas.biblioteca.modelo;

public class Licenca {
    private final int limiteAcessosSimultaneos = 60;
    private int acessosAtivos;

    public Licenca (){
        this.acessosAtivos = 0;
    }
    
    public boolean temVagaDisponivel(Ebook ebook){
        return false;
    } 
    
    public boolean licencaExpirada(){
        return false;
    }

    public int getAcessosAtivos() {
        return acessosAtivos;
    }

    public void setAcessosAtivos(int acessosAtivos) {
        this.acessosAtivos = acessosAtivos;
    }
}
