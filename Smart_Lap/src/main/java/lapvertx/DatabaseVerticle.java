package lapvertx;


import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.mysqlclient.MySQLConnectOptions;
import io.vertx.mysqlclient.MySQLPool;
import io.vertx.sqlclient.PoolOptions;
import io.vertx.sqlclient.Row;
import io.vertx.sqlclient.RowSet;
import Types.Sensores;

public class DatabaseVerticle extends AbstractVerticle{
	
	private MySQLPool mySQLPool;
	
	@Override
	public void start(Promise<Void> startPromise) {
		MySQLConnectOptions mySQLConnectOptions = new MySQLConnectOptions().setPort(3306).setHost("localhost")
				.setDatabase("lap").setUser("root").setPassword("xe0915311b7b2");
		PoolOptions poolOptions = new PoolOptions().setMaxSize(5);
		mySQLPool = MySQLPool.pool(vertx, mySQLConnectOptions, poolOptions);
		
		Router router = Router.router(vertx);
		vertx.createHttpServer().requestHandler(router::handle).listen(8080, result -> {
			if (result.succeeded()) {
				startPromise.complete();
			}else {
				startPromise.fail(result.cause());
			}
		});
		router.get("/lap/dispositivo/edificiocalor/:IdEdificio").handler(this::getpisoscalor);
		router.get("/lap/dispositivo/values/:idDispositivo").handler(this::getValueByDispositivo);
		router.get("/lap/dispositivo/values/:idSensor/:timestamp").handler(this::getValueBySensorAndTimestamp);
		router.get("/lap/sensor/values/:idSensor").handler(this::getValueBySensor);
	}
	
	private void getValueBySensorAndTimestamp(RoutingContext routingContext) {
		mySQLPool.query("SELECT * FROM lap.sensores WHERE timestamp > " + 
						routingContext.request().getParam("timestamp") + " AND idsensor = " + 
						routingContext.request().getParam("idSensor"), 
				res -> {
					if (res.succeeded()) {
						RowSet<Row> resultSet = res.result();
						System.out.println("El número de elementos obtenidos es " + resultSet.size());
						JsonArray result = new JsonArray();
						for (Row row : resultSet) {
							result.add(JsonObject.mapFrom(new Sensores(row.getInteger("idSensor"),
									row.getInteger("idDispositivo"),
									row.getFloat("temperatura"),
									
									row.getFloat("humedad"),
									row.getInteger("calidad_aire"),
									row.getLong("timestamp"),
									row.getInteger("location"))));
						}
						
						routingContext.response().setStatusCode(200).putHeader("content-type", "application/json")
							.end(result.encodePrettily());
					}else {
						routingContext.response().setStatusCode(401).putHeader("content-type", "application/json")
							.end((JsonObject.mapFrom(res.cause()).encodePrettily()));
					}
				});
	}
	private void getValueByDispositivo(RoutingContext routingContext) {
		mySQLPool.query("SELECT * FROM lap.sensores WHERE idDispositivo= " + 
						routingContext.request().getParam("idDispositivo"), 
				res -> {
					if (res.succeeded()) {
						RowSet<Row> resultSet = res.result();
						System.out.println("El número de elementos obtenidos es " + resultSet.size());
						JsonArray result = new JsonArray();
						for (Row row : resultSet) {
							result.add(JsonObject.mapFrom(new Sensores(row.getInteger("idSensor"),
									row.getInteger("idDispositivo"),
									row.getFloat("temperatura"),
									
									row.getFloat("humedad"),
									row.getInteger("calidad_aire"),
									row.getLong("timestamp"),
									row.getInteger("location"))));
						}
						
						routingContext.response().setStatusCode(200).putHeader("content-type", "application/json")
							.end(result.encodePrettily());
					}else {
						routingContext.response().setStatusCode(401).putHeader("content-type", "application/json")
							.end((JsonObject.mapFrom(res.cause()).encodePrettily()));
					}
				});
	}
	private void getpisoscalor(RoutingContext routingContext) {
		mySQLPool.query("SELECT * FROM lap.sensores , lap.edificio, lap.dispositivo WHERE temperatura > 26 and edificio.idEdificio= "+
				routingContext.request().getParam("idEdificio") +" and "
				+ "dispositivo.idDispositivo=sensores.idDispositivo and edificio.idEdificio=dispositivo.idEdificio ;", 
				res -> {
					if (res.succeeded()) {
						RowSet<Row> resultSet = res.result();
						System.out.println("El número de elementos obtenidos es " + resultSet.size());
						JsonArray result = new JsonArray();
						
						for (Row row : resultSet) {
							System.out.println(resultSet);
							Sensores d = new Sensores();
							d.setLocation(row.getInteger("location"));
							result.add(JsonObject.mapFrom(d.getLocation()));
							
						}
						
						routingContext.response().setStatusCode(200).putHeader("content-type", "application/json")
							.end(result.encodePrettily());
					}else {
						routingContext.response().setStatusCode(401).putHeader("content-type", "application/json")
							.end((JsonObject.mapFrom(res.cause()).encodePrettily()));
					}
				});
	}
	private void getValueBySensor(RoutingContext routingContext) {
		mySQLPool.query("SELECT * FROM lap.sensores WHERE idSensor = " + routingContext.request().getParam("idSensor"), 
				res -> {
					if (res.succeeded()) {
						RowSet<Row> resultSet = res.result();
						System.out.println("El número de elementos obtenidos es " + resultSet.size());
						JsonArray result = new JsonArray();
						
						for (Row row : resultSet) {
							System.out.println(row);
							result.add(JsonObject.mapFrom(new Sensores(row.getInteger("idSensor"),
									row.getInteger("idDispositivo"),
									row.getFloat("temperatura"),
									
									row.getFloat("humedad"),
									row.getInteger("calidad_aire"),
									row.getLong("timestamp"),
									row.getInteger("location"))));
							System.out.println(result);
						}
						
						routingContext.response().setStatusCode(200).putHeader("content-type", "application/json")
							.end(result.encodePrettily());
					}else {
						routingContext.response().setStatusCode(401).putHeader("content-type", "application/json")
							.end((JsonObject.mapFrom(res.cause()).encodePrettily()));
					}
				});
	}

}
