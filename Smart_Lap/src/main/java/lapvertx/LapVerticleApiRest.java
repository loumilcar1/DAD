package lapvertx;

import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.Map;

import Types.Humedity;
import Types.Temperature;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.json.Json;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;

public class LapVerticleApiRest extends AbstractVerticle {

	private Map<Integer, Temperature> elements = new LinkedHashMap<Integer, Temperature>();
	private Map<Integer, Humedity> humedity = new LinkedHashMap<Integer, Humedity>();

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
		
		router.route("/api/humedity*").handler(BodyHandler.create());
		router.get("/api/humedity").handler(this::getAll);
		router.put("/api/humedity").handler(this::addOne);
		router.delete("/api/humedity").handler(this::deleteOne);
		router.post("/api/elements/:humedityid").handler(this::postOne);
	}

	private void createSomeData() {
		Temperature temp1 = new Temperature();
		elements.put(temp1.getId(), temp1);
		Temperature temp2 = new Temperature(24,Calendar.getInstance().getTimeInMillis()+1000,"Segundo piso", 2);
		elements.put(temp2.getId(), temp2);
		Temperature temp3 = new Temperature(12,Calendar.getInstance().getTimeInMillis()+500,"Primer piso", 1);
		elements.put(temp3.getId(), temp3);
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

}
