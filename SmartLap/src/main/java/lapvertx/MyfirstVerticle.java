package lapvertx;


import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.http.HttpServerRequest;

public class MyfirstVerticle extends AbstractVerticle{
	//@SuppressWarnings("deprecation")
	@Override
	public void start(Future<Void> startFuture) {
	 vertx.createHttpServer().requestHandler(
			 request ->{
				 request.response().end("<h1>Bienvenido</h1> Hola mundo<br>");
			 }
		
	 ).listen(8081,result -> {
		 if(result.succeeded()) {
			 System.out.println("todo correcto");
		 }else {
			 System.out.print(result.cause());
		 }
	 });
	vertx.deployVerticle(DatabaseVerticle.class.getName());
	vertx.deployVerticle(Telegrambot.class.getName());
	vertx.deployVerticle(MqttServerVerticle.class.getName());
	vertx.deployVerticle(MqttClientVerticle.class.getName());
	vertx.deployVerticle(MqttClientVerticle.class.getName());
	// vertx.deployVerticle(.class.getName());
	/* vertx.deployVerticle(mySecondVerticle.class.getName());
	 vertx.deployVerticle(mythirdVerticle.class.getName());fourverticle
	 
	 EventBus eventBUs= vertx.eventBus();
	 vertx.setPeriodic(4000, action -> {
		eventBUs.send("mensaje_p2p", "Hola esto es un mensaje ¿te llega?", reply-> {
			if (reply.succeeded()) {
				String replymesage = (String ) reply.result().body();
				System.out.println("respuesta : "+ replymesage);
			}
			else {
				System.out.println("no hay respuesta");
			}
		}); 
	 });
	 vertx.setPeriodic(4000,action->{
		eventBUs.publish("mensaje_brotcas", "Esto es un mensaje broadcast"); 
	 });*/
	}
}
