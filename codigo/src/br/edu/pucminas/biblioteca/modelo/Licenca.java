package br.edu.pucminas.biblioteca.modelo;

public class Licenca {
    private final int limiteAcessosSimultaneos = 60;
    private int acessosAtivos;

    public Licenca (){
        this.acessosAtivos = 0;
    }
    
    public boolean temVagaDisponivel(Ebook ebook){
        return (acessosAtivos < limiteAcessosSimultaneos);
    } 
    
    public boolean licencaExpirada(){
        // TODO: implementar na Sprint 3
        return false;
    }

    public void incrementarAcessosAtivos(){
        this.acessosAtivos++;
    }

    public void decrementarAcessosAtivos(){
        this.acessosAtivos--;
    }

    public int getAcessosAtivos() {
        return acessosAtivos;
    }
}
