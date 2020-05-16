/*package dad.us.dadVertx.apiRestExample;

import java.util.LinkedHashMap;
import java.util.Map;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.json.Json;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;

public class DadVerticleApiRest extends AbstractVerticle {

	private Map<Integer, DomoState> elements = new LinkedHashMap<>();

	@Override
	public void start(Future<Void> startFuture) {
		createSomeData();
		Router router = Router.router(vertx);
		vertx.createHttpServer().requestHandler(router::accept).listen(8080, result -> {
			if (result.succeeded()) {
				startFuture.complete();
			} else {
				startFuture.fail(result.cause());
			}
		});
		router.route("/api/elements*").handler(BodyHandler.create());
		router.get("/api/elements").handler(this::getAll);
		router.put("/api/elements").handler(this::addOne);
		router.delete("/api/elements").handler(this::deleteOne);
		router.post("/api/elements/:elementid").handler(this::postOne);
	}

	private void createSomeData() {
		DomoState light1 = new DomoState("Luz salón", "Light", "Encendida");
		elements.put(light1.getId(), light1);
		DomoState light2 = new DomoState("Luz pasillo", "Light", "Apagada");
		elements.put(light2.getId(), light2);
		DomoState tv1 = new DomoState("TV dormitorio", "Multimedia", "Encendida");
		elements.put(tv1.getId(), tv1);
	}

	private void getAll(RoutingContext routingContext) {
		routingContext.response().putHeader("content-type", "application/json; charset=utf-8")
				.end(Json.encodePrettily(elements.values()));
	}

	private void addOne(RoutingContext routingContext) {
		final DomoState element = Json.decodeValue(routingContext.getBodyAsString(), DomoState.class);
		elements.put(element.getId(), element);
		routingContext.response().setStatusCode(201).putHeader("content-type", "application/json; charset=utf-8")
				.end(Json.encodePrettily(element));
	}
	
	private void postOne(RoutingContext routingContext) {
		int id = Integer.parseInt(routingContext.request().getParam("elementid"));
		DomoState ds = elements.get(id);
		final DomoState element = Json.decodeValue(routingContext.getBodyAsString(), DomoState.class);
		ds.setName(element.getName());
		ds.setType(element.getType());
		ds.setValue(element.getValue());
		elements.put(ds.getId(), ds);
		routingContext.response().setStatusCode(201).putHeader("content-type", "application/json; charset=utf-8")
				.end(Json.encode(element));
	}

	private void deleteOne(RoutingContext routingContext) {
		final DomoState element = Json.decodeValue(routingContext.getBodyAsString(), DomoState.class);
		elements.remove(element.getId());
		routingContext.response().setStatusCode(201).putHeader("content-type", "application/json; charset=utf-8")
				.end(Json.encodePrettily(element));
	}

	@Override
	public void stop(Future<Void> stopFuture) throws Exception {
		super.stop(stopFuture);
	}

}*/