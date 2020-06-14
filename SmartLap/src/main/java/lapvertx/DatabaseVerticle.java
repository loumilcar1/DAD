package lapvertx;


import Types.Edificio;
import Types.Sensores;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.mysqlclient.MySQLClient;
import io.vertx.mysqlclient.MySQLConnectOptions;
import io.vertx.mysqlclient.MySQLPool;
import io.vertx.sqlclient.PoolOptions;
import io.vertx.sqlclient.Row;
import io.vertx.sqlclient.RowSet;
import io.vertx.sqlclient.Tuple;

public class DatabaseVerticle extends AbstractVerticle{
	
	private MySQLPool mySQLPool;
	
	@Override
	public void start(Promise<Void> startPromise) {
		MySQLConnectOptions mySQLConnectOptions = new MySQLConnectOptions().setPort(3306).setHost("localhost")
				.setDatabase("lap").setUser("root").setPassword("xe0915311b7b2");
		PoolOptions poolOptions = new PoolOptions().setMaxSize(5);
		mySQLPool = MySQLPool.pool(vertx, mySQLConnectOptions, poolOptions);
		
		Router router = Router.router(vertx);
		router.route().handler(BodyHandler.create());
		vertx.createHttpServer().requestHandler(router::handle).listen(8080, result -> {
			if (result.succeeded()) {
				startPromise.complete();
			}else {
				startPromise.fail(result.cause());
			}
		});
		
		router.get("/lap/dispositivo/pisocalor/:IdEdificio/:location").handler(this::getpisodondehacecalor);
		router.get("/lap/dispositivo/pisofrio/:IdEdificio/:location").handler(this::getpisodondehacefrio);
		router.get("/lap/dispositivo/edificiofrio/:IdEdificio").handler(this::getpisosdondehacefrio);
		router.get("/lap/dispositivo/edificiocalor/:IdEdificio").handler(this::getpisoscalor);
		router.get("/lap/dispositivo/values/:idDispositivo").handler(this::getValueByDispositivo);
		router.get("/lap/dispositivo/values/:idSensor/:timestamp").handler(this::getValueBySensorAndTimestamp);
		router.get("/lap/sensor/values/:idSensor").handler(this::getValueBySensor);
		router.get("/lap/dispositivo/values/:IdEdificio/:location").handler(this::getValueByPiso);
		router.get("/lap/dispositivo/value/:IdEdificio").handler(this::getValueByEdificio);
		router.get("/lap/sensor/borrarEdificio/:idEdificio").handler(this::deleteEdificio);//borra cuidado 
		router.put("/lap/sensor/values").handler(this::putvalueSensor);
		router.put("/lap/edificio").handler(this::putEdificio);
	}

	private void putvalueSensor(RoutingContext context) {
		Sensores sensores = Json.decodeValue(context.getBodyAsString(), Sensores.class);
		mySQLPool.preparedQuery(
				"INSERT INTO sensores (idDispositivo, temperatura, humedad, calidad_aire, timestamp, location) VALUES (?, ?, ?, ?, ?, ?);",
				Tuple.of(sensores.getIdDispositivo(), sensores.getTemperatura(), sensores.getHumedad(),
						sensores.getCalidad_aire(), sensores.getTimestamp(), sensores.getLocation()),
				handler -> {
					if (handler.succeeded()) {
						System.out.println(handler.result().rowCount());
						Long id = handler.result().property(MySQLClient.LAST_INSERTED_ID);
						sensores.setIdSensor(id.intValue());
						context.response().setStatusCode(200).putHeader("content-type", "application/json")
								.end(JsonObject.mapFrom(sensores).encodePrettily());
						;
					} else {
						System.out.println(handler.cause().toString());
						context.response().setStatusCode(401).putHeader("content-type", "application/json")
								.end((JsonObject.mapFrom(handler.cause()).encodePrettily()));
					}
				});

	}

	private void putEdificio(RoutingContext context) {
		Edificio edificio = Json.decodeValue(context.getBodyAsString(), Edificio.class);
		mySQLPool.preparedQuery(
				"INSERT INTO `lap`.`edificio` ( `direccion`, `dni`, `nombre`, `apellidos`, `telefono`, `numPlantas`) VALUES ( ?, ?, ?, ?, ?, ?);",
				Tuple.of( edificio.getDireccion(), edificio.getDni(), edificio.getNombre(),
						edificio.getApellidos(), edificio.getTelefono(), edificio.getNumPlantas()),
				handler -> {
					if (handler.succeeded()) {
						System.out.println(handler.result().rowCount());
						Long id = handler.result().property(MySQLClient.LAST_INSERTED_ID);
						edificio.setIdEdificio(id.intValue());
						context.response().setStatusCode(200).putHeader("content-type", "application/json")
								.end(JsonObject.mapFrom(edificio).encodePrettily());
						;
					} else {
						System.out.println(handler.cause().toString());
						context.response().setStatusCode(401).putHeader("content-type", "application/json")
								.end((JsonObject.mapFrom(handler.cause()).encodePrettily()));
					}
				});

	}

	
private void getpisodondehacecalor(RoutingContext routingContext) {
		
		mySQLPool.query("SELECT  sensores.*  FROM lap.sensores,  dispositivo  where  dispositivo.idDispositivo=sensores.idDispositivo and idEdificio="+routingContext.request().getParam("IdEdificio")+" and location="+routingContext.request().getParam("location")+" and temperatura> 28 group by location order by timestamp desc limit 100"
						+routingContext.request().getParam("idEdificio") + routingContext.request().getParam("location") , 
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
private void getpisodondehacefrio(RoutingContext routingContext) {
	
	mySQLPool.query("SELECT  sensores.*  FROM lap.sensores,  dispositivo  where  dispositivo.idDispositivo=sensores.idDispositivo and idEdificio="+routingContext.request().getParam("IdEdificio")+" and location="+routingContext.request().getParam("location")+" and temperatura< 26 group by location order by timestamp desc limit 100"
					+routingContext.request().getParam("idEdificio") +routingContext.request().getParam("location"), 
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
	private void getpisosdondehacefrio(RoutingContext routingContext) {
		
		mySQLPool.query("SELECT  sensores.*  FROM lap.sensores,  dispositivo  where  dispositivo.idDispositivo=sensores.idDispositivo and idEdificio="+routingContext.request().getParam("IdEdificio")+"  and temperatura< 20 group by location order by timestamp desc limit 100"
						+routingContext.request().getParam("idEdificio"), 
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
	
	private void getValueByPiso(RoutingContext routingContext) {
		mySQLPool.query("SELECT sensores.idSensor,sensores.idDispositivo,sensores.temperatura,sensores.humedad,sensores.calidad_aire, sensores.timestamp,sensores.location FROM sensores inner join dispositivo on sensores.idDispositivo = dispositivo.idDispositivo "
				+ "inner join edificio on dispositivo.idEdificio=edificio.idEdificio where edificio.idEdificio="+routingContext.request().getParam("idEdificio")+" and sensores.location="+routingContext.request().getParam("location"), 
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
	
	private void getValueByEdificio(RoutingContext routingContext) {
		mySQLPool.query("select * from sensores,dispositivo where sensores.idDispositivo=dispositivo.idDispositivo and dispositivo.idEdificio="+routingContext.request().getParam("idEdificio")+"  group by location order by timestamp desc",res -> {
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
	
	private void deleteEdificio(RoutingContext routingContext) {
		mySQLPool.query("DELETE  FROM lap.edificio WHERE edificio.idEdificio="+routingContext.request().getParam("idEdificio"), 
				res -> {
					if (res.succeeded()) {
					System.out.println("Se ha borrado el edificio "+routingContext.request().getParam("idEdificio")+".");
					routingContext.response().setStatusCode(200).putHeader("content-type", "application/json")
					.end();}else {
						routingContext.response().setStatusCode(401).putHeader("content-type", "application/json")
						.end((JsonObject.mapFrom(res.cause()).encodePrettily()));
					}
					
				});
	}
}