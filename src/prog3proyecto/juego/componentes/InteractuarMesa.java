package prog3proyecto.juego.componentes;

import org.joml.Quaternionf;
import org.joml.Vector3f;

import com.lndf.glengine.scene.GameObject;

import prog3proyecto.juego.Juego;

public class InteractuarMesa extends InteractConObjeto {

	private GameObject posarLibro1;
	private GameObject posarLibro2;
	private GameObject posarLibro3;
	private int contador;
	
	public InteractuarMesa(GameObject jugador, GameObject posarLibro1, GameObject posarLibro2, GameObject posarLibro3) {
		super(jugador);
		this.setDistancia(3);
		this.posarLibro1 = posarLibro1;
		this.posarLibro2 = posarLibro2;
		this.posarLibro3 = posarLibro3;	
		this.contador = 0;
	}
	
	@Override
	public void entrarInteract() {
		super.entrarInteract();
		ObjetoLlevable.setInteractActivado(false);
	}
	
	@Override
	public void salirInteract() {
		super.salirInteract();
		ObjetoLlevable.setInteractActivado(true);
	}
	
	@Override
	public void interactuar() {
		ObjetoLlevable llevado = ObjetoLlevable.getObjetoLlevando();
		if(llevado!=null) {
			GameObject posar = null;
			switch(contador) {
			case 0:
				posar = posarLibro1;
				break;
			case 1:
				posar = posarLibro3;
				break;
			case 2:
				posar = posarLibro2;
				Juego.escena.getTerreno().movilizarElevador();
				Juego.escena.getJugador().removeComponent(Juego.escena.getJugador().getComponent(Fase1.class));
				Juego.escena.getJugador().addComponent(new Fase2());
				break;
			}
			this.contador++;
			GameObject objeto = llevado.getGameObject();
			llevado.soltar();
			objeto.removeComponent(llevado);
			Vector3f p = posar.getTransform().getWorldPosition();
			p.y += 0.3f;
			objeto.getTransform().setWorldPosition(p);
			objeto.getTransform().setWorldRotation(new Quaternionf());
		}
	}
	

}