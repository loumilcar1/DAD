package lapvertx;



import org.schors.vertx.telegram.bot.LongPollingReceiver;
import org.schors.vertx.telegram.bot.TelegramBot;
import org.schors.vertx.telegram.bot.TelegramOptions;
import org.schors.vertx.telegram.bot.api.methods.SendMessage;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.HttpResponse;
import io.vertx.ext.web.client.WebClient;

public class Telegrambot extends AbstractVerticle{
	private TelegramBot bot;
	private String variable=null;
	@Override
	public void start(Promise<Void> future) {
		TelegramOptions options=new TelegramOptions().setBotName("Vertexgusdad_bot").setBotToken("1113833434:AAH2H8GjXEnuWkPXWC_rvSV2zYjR2BI7HcY");
		bot =TelegramBot.create(vertx,options).receiver(new LongPollingReceiver().onUpdate(handler->{
			if(handler.getMessage().getText().toLowerCase().contains("hola")) {
				bot.sendMessage(new SendMessage().setText("Hola "+ handler.getMessage().getFrom().getFirstName()+" ¿En que puedo ayudarte?")
						.setChatId(handler.getMessage().getChatId()));
				variable =null;
			}else if (variable==null){
				if(handler.getMessage().getText().toLowerCase().contains("calor")) {
					bot.sendMessage(new SendMessage().setText("¿de que piso?").setChatId(handler.getMessage().getChatId()));
					variable="calor";}
				else if(handler.getMessage().getText().toLowerCase().contains("frio")) {
						bot.sendMessage(new SendMessage().setText("¿de que piso?").setChatId(handler.getMessage().getChatId()));
						variable="frio";
				}
				else if(handler.getMessage().getText().toLowerCase().contains("aire")) {
					bot.sendMessage(new SendMessage().setText("¿de que piso?").setChatId(handler.getMessage().getChatId()));
					variable="contaminacion";
				}
				else if(handler.getMessage().getText().toLowerCase().contains("tempe")) {
					bot.sendMessage(new SendMessage().setText("¿de que piso?").setChatId(handler.getMessage().getChatId()));
					variable="tempe";
				
				}else if(handler.getMessage().getText().toLowerCase().contains("hume")) {
					bot.sendMessage(new SendMessage().setText("¿de que piso?").setChatId(handler.getMessage().getChatId()));
					variable="hume";
				
				}
			}else {
				if(variable=="calor" ) {
				WebClient client= WebClient.create(vertx);
				
				client.get(8080,"localhost","/lap/dispositivo/pisocalor/1/"+handler.getMessage().getText()).send(ar->{
					if(ar.succeeded()) {
						HttpResponse<Buffer> response =ar.result();
						JsonArray object =response.bodyAsJsonArray();
						System.out.println(object);
						
						if (object.isEmpty()) {
							
							bot.sendMessage(new SendMessage().setText("No hace demasioado calor ").setChatId(handler.getMessage().getChatId()));
							variable=null;
						}else {
							Float temp=  object.getJsonObject(0).getFloat("temperatura");
							bot.sendMessage(new SendMessage().setText("Si hace calor algo de calor , la temperatura es de "+temp+". ").setChatId(handler.getMessage().getChatId()));
							variable=null;
									
						}
						
					}else {
						bot.sendMessage(new SendMessage().setText("Algo ha salido mal ").setChatId(handler.getMessage().getChatId()));
						variable=null;
					}
				});;
			}
			else if(variable=="hume" ) {
				WebClient client= WebClient.create(vertx);
				
				client.get(8080,"localhost","/lap/dispositivo/values/1/"+handler.getMessage().getText()).send(ar->{
					if(ar.succeeded()) {
						HttpResponse<Buffer> response =ar.result();
						JsonArray object =response.bodyAsJsonArray();
						System.out.println(object);
							
						 if(object.isEmpty()) {
							 bot.sendMessage(new SendMessage().setText("NO hay datos todavia ").setChatId(handler.getMessage().getChatId()));
								variable=null;
						 }else {
							Float hum=  object.getJsonObject(0).getFloat("humedad");
							if(hum>60) {
							bot.sendMessage(new SendMessage().setText("Hay demasiada humedad con " + hum+ "% de humedad").setChatId(handler.getMessage().getChatId()));
							variable=null;
							}
							else if(hum<15){
								bot.sendMessage(new SendMessage().setText("Aqui esta un poco secoel hambiente con " + hum+ "% de humedad").setChatId(handler.getMessage().getChatId()));
								variable=null;
							}
							else {
								bot.sendMessage(new SendMessage().setText("Estamos con " + hum+ "% de humedad").setChatId(handler.getMessage().getChatId()));
								variable=null;
							}}
									
						
						
					}else {
						bot.sendMessage(new SendMessage().setText("Algo ha salido mal ").setChatId(handler.getMessage().getChatId()));
						variable=null;
					}
				});;
			}			else if(variable=="tempe" ) {
				WebClient client= WebClient.create(vertx);
				
				client.get(8080,"localhost","/lap/dispositivo/values/1/"+handler.getMessage().getText()).send(ar->{
					if(ar.succeeded()) {
						HttpResponse<Buffer> response =ar.result();
						JsonArray object =response.bodyAsJsonArray();
						System.out.println(object);
							
						 if(object.isEmpty()) {
							 bot.sendMessage(new SendMessage().setText("No hay datos todavia").setChatId(handler.getMessage().getChatId()));
								variable=null;
						 }else {
							Float hum=  object.getJsonObject(0).getFloat("temperatura");
							if(hum>30) {
							bot.sendMessage(new SendMessage().setText("Aqui hace un podo de calor con " + hum+ "º de temperatura").setChatId(handler.getMessage().getChatId()));
							variable=null;
							}
							else if(hum<10){
								bot.sendMessage(new SendMessage().setText("Aqui hace mucho frio con " + hum+ "º de temperatura").setChatId(handler.getMessage().getChatId()));
								variable=null;
							}
							else {
								bot.sendMessage(new SendMessage().setText("Estamos con " + hum+ "º de temperatura").setChatId(handler.getMessage().getChatId()));
								variable=null;
							}}
									
						
						
					}else {
						bot.sendMessage(new SendMessage().setText("Algo ha salido mal").setChatId(handler.getMessage().getChatId()));
						variable=null;
					}
				});;
			}else if(variable=="frio" ) {
				WebClient client= WebClient.create(vertx);
				
				client.get(8080,"localhost","/lap/dispositivo/pisofrio/1/"+handler.getMessage().getText()).send(ar->{
					if(ar.succeeded()) {
						HttpResponse<Buffer> response =ar.result();
						JsonArray object =response.bodyAsJsonArray();
						System.out.println(object);
							
						 if(object.isEmpty()) {
							 bot.sendMessage(new SendMessage().setText("No no hace frio ").setChatId(handler.getMessage().getChatId()));
								variable=null;
						 }
							Float temp=  object.getJsonObject(0).getFloat("temperatura");
							bot.sendMessage(new SendMessage().setText("Hace un poco de fresco, la temperatura es de "+temp+".").setChatId(handler.getMessage().getChatId()));
							variable=null;
									
						
						
					}else {
						bot.sendMessage(new SendMessage().setText("Algo ha salido mal").setChatId(handler.getMessage().getChatId()));
						variable=null;
					}
				});;
			};
		}
			}));
		bot.start();
	}
}
