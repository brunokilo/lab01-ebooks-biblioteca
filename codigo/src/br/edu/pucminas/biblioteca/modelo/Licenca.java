package br.edu.pucminas.biblioteca.modelo;

public class Licenca {
    //TODO implementar o resto das exceções
    private final int limiteAcessosSimultaneos = 60;
    private final int minimoAcessosPorSemestre = 3;
    private int acessosAtivos;

    public Licenca (){
        this.acessosAtivos = 0;
    }
    
    public boolean temVagaDisponivel(Ebook ebook){
        return (acessosAtivos < limiteAcessosSimultaneos);
    } 
    
    public boolean acessosAtivosMenorQueMinimoPermitido(){
        return (acessosAtivos < minimoAcessosPorSemestre);
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
