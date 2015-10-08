package com.gabrielfeitosa.pedidos;

import java.util.List;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

@Path("/lanchonete")
public class LanchoneteService {

	private static boolean atendimentoAberto = false;
	private static int index = 0;

	@POST
	@Path("/iniciarAtendimento")
	public void iniciarAtendimento() {
		if (!atendimentoAberto) {
			atendimentoAberto = true;
			mockarCadastroPedidos();
		}

	}

	@POST
	@Path("/encerrarAtendimento")
	public void encerrarAtendimento(){
		atendimentoAberto = false;
		index = 0;
	}
	
	@GET
	@Path("/pedidos")
	@Produces("application/json")
	public List<Pedido> getPedidos() {
		return PedidosBD.getPedidos();
	}

	@DELETE
	@Path("/pedidos/{id}")
	public void atenderPedido(@PathParam("id") Integer id) {
		PedidosBD.remover(id);
	}
	
	@POST
	@Path("/pedidos")
	public void cadastrarPedido(Pedido pedido){
		PedidosBD.cadastrarPedido(pedido);		
	}
	
	private void mockarCadastroPedidos() {
		new Thread() {
			@Override
			public void run() {
				while (atendimentoAberto) {
					cadastrarPedido(new Pedido(index, "Item " + index++));
					try {
						Thread.sleep((int)(Math.random()*10000));
					} catch (InterruptedException e) {
						System.err.println("Ops, acabou a farinha!");
					}
				}
				PedidosBD.zerarPedidos();
			}
		}.start();
	}

}
