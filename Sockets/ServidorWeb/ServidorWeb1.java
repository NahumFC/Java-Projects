import java.net.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.io.*;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.swing.JOptionPane;

 class ServidorWeb1
{
	public static final int PUERTO=8000;
	ServerSocket ss;
	ExecutorService ejecutorSubprocesos;

		
		class Manejador implements Runnable
		{
			protected Socket socket;

            DataOutputStream dos;
            DataInputStream dis;
			protected String FileName;
			int contBytes;
			List<String> parametro;
			List<String> valor;

			
			public Manejador(Socket _socket) throws Exception
			{
				this.socket=_socket;
			}
			
			public void run()
			{
				try{

                    dos = new DataOutputStream(socket.getOutputStream());//Se utiliza para enviar datos al cliente (Navegador)
                    dis = new DataInputStream(socket.getInputStream());//Se utiliza para recibir datos del cliente 
                    valor = new ArrayList<>();
                    parametro = new ArrayList<>();
                    contBytes=0;
                    
                    byte[] b = new byte[50000*2];
                    int t = dis.read(b);//Lee los datos enviados por el cliente y los almacena en b. Devuelve el número de bytes leídos (t).
                    
                    
                    /*---------------------------------------------------------------------------------------------*/
                    String peticion=null;
                    if(t>0) {
                    	peticion = new String(b,0,t);//Convierte los bytes leídos en una cadena que contiene la solicitud HTTP del cliente.
                    }
                    /*---------------------------------------------------------------------------------------------*/
                    //String peticion = new String(b,0,t);//Convierte los bytes leídos en una cadena que contiene la solicitud HTTP del cliente.
                    
                    
					System.out.println("t: "+t);

					if(peticion==null)
					{
						StringBuffer sb = new StringBuffer();
                        sb.append("<html><head><title>Servidor WEB\n");
						sb.append("</title><body bgcolor=\"#AACCFF\"<br>Linea Vacia</br>\n");
						sb.append("</body></html>\n");
                        dos.write(sb.toString().getBytes());
                        dos.flush();
						socket.close();
						return;
					}
					System.out.println("\nCliente Conectado desde: "+socket.getInetAddress());
					System.out.println("Por el puerto: "+socket.getPort());
					System.out.println("Datos: "+peticion+"\r\n\r\n");
                                        
					StringTokenizer st1= new StringTokenizer(peticion,"\n");//Divide la solicitud por líneas y extrae la primera línea (línea de solicitud)
                    String line = st1.nextToken();
                    contBytes+=line.getBytes().length;

                    if(line.toUpperCase().startsWith("GET")) {
                    	
    					if(line.indexOf("?")==-1) //No contiene parametros
    					{    						
    						getArch(line); //Obtiene el nombre del archivo
    						if(FileName.compareTo("")==0) //No especifico archivo
    						{
    							SendA("index.htm",dos);
    						}
    						else //Especifico Archivo
    						{
    							SendA(FileName,dos);
    						}  												
    					}
    					
    					else {
    						StringTokenizer tokens=new StringTokenizer(line,"?");
    						String req_a=tokens.nextToken();// "GET /busqueda"
    						String req=tokens.nextToken();// "nombre=Juan&edad=25 HTTP/1.1"
    						System.out.println("Token1: "+req_a);
    						System.out.println("Token2: "+req);
                                                    String parametros = req.substring(0, req.indexOf(" "))+"\n"; // "nombre=Juan&edad=25\n"
                                                    System.out.println("parametros: "+parametros);
                                                    StringBuffer respuesta= new StringBuffer(); //Crea un buffer para ir agregando los elementos 
                                                    
                                                    respuesta.append("HTTP/1.0 200 Okay \n");
                                                    String fecha= "Date: " + new Date()+" \n";
                                                    respuesta.append(fecha);
                                                    String tipo_mime = "Content-Type: text/html \n\n";
                                                    respuesta.append(tipo_mime);
                                                    respuesta.append("<html><head><title>SERVIDOR WEB</title></head>\n");
                                                    respuesta.append("<body bgcolor=\"#AACCFF\"><center><h1><br>Parametros Obtenidos..</br></h1><h3><b>\n");
                                                    respuesta.append(parametros);
                                                    respuesta.append("</b></h3>\n");
                                                    respuesta.append("</center></body></html>\n\n");
                                                    System.out.println("Respuesta: "+respuesta);
                                                    dos.write(respuesta.toString().getBytes()); //Manda la respuesta
                                                    dos.flush();
                                                    dos.close();
                                                    socket.close();
    					}
                    }
					else if(line.toUpperCase().startsWith("PUT")) //Procesa en caso de haber pasado parametros http://localhost:8000/busqueda?nombre=Juan&edad=25
					{
						
						String archExt=null;
						do {
							line = st1.nextToken();
							contBytes+=line.getBytes().length;
							if(line.startsWith("Content-Disposition")) {
								archExt=line.substring(line.lastIndexOf("=")+2, line.lastIndexOf("\""));
								break;
							}				
						}while(st1.hasMoreTokens());
						
						System.out.println("Archivo: >> " + archExt);
						
						line = st1.nextToken();
						contBytes+=line.getBytes().length;
						line = st1.nextToken();
						contBytes+=line.getBytes().length;
					
						System.out.println("Se leyeron: " + contBytes);
						System.out.println("Tamaño Buffer: " + b.length);
						System.out.println("Ocupado: " + t);
						
						
						byte[] array=Arrays.copyOfRange(b, contBytes+14,t-58);
						
						File archivoReconstruido = new File("/home/nfive-elite/Downloads/"+archExt);
			            Path rutaArchivoReconstruido = archivoReconstruido.toPath();
			            Files.write(rutaArchivoReconstruido, array, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
						
					    String respuesta = "HTTP/1.1 200 OK\r\n" +
			                       "Content-Type: text/html\r\n\r\n" +
			                       "<html><head><title>SERVIDOR WEB</title></head>\r\n"
			                       + "<body bgcolor=\"#AACCFF\"><center><h1><br>ARCHIVO SUBIDO CORRECTAMENTE</br></h1>\r\n"
			                       + "</center></body></html>";
					    dos.write(respuesta.getBytes());
					    dos.flush();
					    dos.close();
					    socket.close();
					    

					}
					else if(line.toUpperCase().startsWith("DELETE")) {
						
						getArch(line);
						
						String archEl="/home/nfive-elite/Downloads/"+FileName;
						System.out.println("Archivo a eliminar>>> "+archEl);
						
					    File archivo = new File(archEl);
					    String respuesta;
					    
					    if (archivo.exists()) { // Verifica si el archivo existe
				            if (archivo.delete()) {
				            	 // Respuesta al cliente
							    respuesta = "HTTP/1.1 200 OK\r\n" +
		                       "Content-Type: text/html\r\n\r\n" +
		                       "<html><head><title>SERVIDOR WEB</title></head>\r\n"
		                       + "<body bgcolor=\"#AACCFF\"><center><h1><br>ARCHIVO ELIMINADO CORRECTAMENTE</br></h1>\r\n"
		                       + "</center></body></html>";
				            } else {
				            	 // Respuesta al cliente
							    respuesta = "HTTP/1.1 200 OK\r\n" +
		                       "Content-Type: text/html\r\n\r\n" +
		                       "<html><head><title>SERVIDOR WEB</title></head>\r\n"
		                       + "<body bgcolor=\"#AACCFF\"><center><h1><br>NO SE PUDO ELIMINAR EL ARCHIVO</br></h1>\r\n"
		                       + "</center></body></html>";
				            }
				        } else {
						    respuesta = "HTTP/1.1 200 OK\r\n" +
				                       "Content-Type: text/html\r\n\r\n" +
				                       "<html><head><title>SERVIDOR WEB</title></head>\r\n"
				                       + "<body bgcolor=\"#AACCFF\"><center><h1><br>EL ARCHIVO NO EXISTE</br></h1>\r\n"
				                       + "</center></body></html>";
				        }
						
						 
					    dos.write(respuesta.getBytes());
					    dos.flush();
					    dos.close();
					    socket.close();
					    
					}
					else if(line.toUpperCase().startsWith("HEAD")) {
						
    					if(line.indexOf("?")==-1) //No contiene el caracter ? en la solicitud
    					{    						
    						getArch(line); //Obtiene el nombre del archivo
    						//System.out.println("Valor archivo: "+FileName);
    						if(FileName.compareTo("")==0) //No especifico archivo
    						{
    							SendASB("index.htm",dos);
    						}
    						else //Especifico Archivo
    						{
    							SendASB(FileName,dos);
    						}  												
    					}

					    socket.close();
					}
					else if(line.toUpperCase().startsWith("POST")) {
						

						if(line.indexOf("?")==1) {
							System.out.println("PETICION INCORRECTA");
						}
						
						else {
							
							do {
								line = st1.nextToken();
								if(line.startsWith("Content-Length")) {
									line = st1.nextToken();
									break;
								}				
							}while(st1.hasMoreTokens());
							
							if(!st1.hasMoreTokens()) {
								////////////////////////////////////////////////////////////////////
	                            StringBuffer respuesta= new StringBuffer(); //Crea un buffer para ir agregando los elementos 
	                            
	                            respuesta.append("HTTP/1.0 200 Okay \n");
	                            String fecha= "Date: " + new Date()+" \n";
	                            respuesta.append(fecha);
	                            String tipo_mime = "Content-Type: text/html \n\n";
	                            respuesta.append(tipo_mime);
	                            respuesta.append("<html><head><title>SERVIDOR WEB</title></head>\n");
	                            respuesta.append("<body bgcolor=\"#AACCFF\"><center><h1><br>Parametros Obtenidos..</br></h1><h3><b>\n");
	                            respuesta.append("SE RECIBIO UNA SOLICITUD SIN PARAMETROS");
	                            respuesta.append("</b></h3>\n");
	                            respuesta.append("</center></body></html>\n\n");
	                            System.out.println("Respuesta: "+respuesta);
	                            dos.write(respuesta.toString().getBytes()); //Manda la respuesta
	                            dos.flush();
	                            dos.close();
	                            socket.close();	                           
								////////////////////////////////////////////////////////////////////
							}
							
							else {
								do {
									line = st1.nextToken();
									if(line.startsWith("----------------------------")) {
										if(!st1.hasMoreTokens()) {
											break;
										}
										line = st1.nextToken();
										parametro.add(line.substring(line.lastIndexOf("=")+2, line.lastIndexOf("\"")));
										line = st1.nextToken();
										line = st1.nextToken();
										valor.add(line);
									}				
								}while(st1.hasMoreTokens());
								
								System.out.println("Parametros: \n"+parametro.toString()+"   "+valor.toString());
								
								////////////////////////////////////////////////////////////////////
	                            StringBuffer respuesta= new StringBuffer(); //Crea un buffer para ir agregando los elementos 
	                            
	                            respuesta.append("HTTP/1.0 200 Okay \n");
	                            String fecha= "Date: " + new Date()+" \n";
	                            respuesta.append(fecha);
	                            String tipo_mime = "Content-Type: text/html \n\n";
	                            respuesta.append(tipo_mime);
	                            respuesta.append("<html><head><title>SERVIDOR WEB</title></head>\n");
	                            respuesta.append("<body bgcolor=\"#AACCFF\"><center><h1><br>Parametros Obtenidos..</br></h1><h3><b>\n");
	                            respuesta.append("Parametros: "+parametro.toString()+" Valores: "+valor.toString());
	                            respuesta.append("</b></h3>\n");
	                            respuesta.append("</center></body></html>\n\n");
	                            System.out.println("Respuesta: "+respuesta);
	                            dos.write(respuesta.toString().getBytes()); //Manda la respuesta
	                            dos.flush();
	                            dos.close();
	                            socket.close();
								////////////////////////////////////////////////////////////////////
							}
														

						}
					}
					else
					{
						dos.write("HTTP/1.0 501 Not Implemented\r\n".getBytes());
                                                dos.flush();
                                                dos.close();
                                                socket.close();
					}

				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
				contBytes=0;

			}//run
			
			public void getArch(String line)
			{
				int i;
				int f;
				if(line.toUpperCase().startsWith("GET") || line.toUpperCase().startsWith("DELETE") || line.toUpperCase().startsWith("HEAD"))
				{
					i=line.indexOf("/");
					f=line.indexOf(" ",i);
					FileName=line.substring(i+1,f);
				}
			}
			
			
			public void SendA(String arg, DataOutputStream dos1) 
			{
                 try{
                	 
				     int b_leidos=0;
	                 DataInputStream dis2 = new DataInputStream(new FileInputStream("C:\\Users\\licea\\OneDrive\\Escritorio\\ServidorWEB\\"+arg));
	
	                 byte[] buf=new byte[1024];
	                 int x=0;
	                 File ff = new File("/home/nfive-elite/Downloads"+arg);			
	                 long tam_archivo=ff.length(),cont=0;
	                 
	                 // Determinar el Content-Type basado en la extensión del archivo
	                 String mimeType = "application/octet-stream";  // Predeterminado
	                 if (arg.endsWith(".jpg") || arg.endsWith(".jpeg")) {
	                	    mimeType = "image/jpeg";
                	} else if (arg.endsWith(".png")) {
                	    mimeType = "image/png";
                	} else if (arg.endsWith(".gif")) {
                	    mimeType = "image/gif";
                	} else if (arg.endsWith(".html") || arg.endsWith(".htm")) {
                	    mimeType = "text/html";
                	} else if (arg.endsWith(".css")) {
                	    mimeType = "text/css";
                	} else if (arg.endsWith(".js")) {
                	    mimeType = "application/javascript";
                	} else if (arg.endsWith(".pdf")) {
                	    mimeType = "application/pdf";
                	} else if (arg.endsWith(".mp4")) {
                	    mimeType = "video/mp4";
                	} else if (arg.endsWith(".mp3")) {
                	    mimeType = "audio/mpeg";
                	} else if (arg.endsWith(".wav")) {
                	    mimeType = "audio/wav";
                	} else if (arg.endsWith(".avi")) {
                	    mimeType = "video/x-msvideo";
                	} else if (arg.endsWith(".zip")) {
                	    mimeType = "application/zip";
                	} else if (arg.endsWith(".json")) {
                	    mimeType = "application/json";
                	} else if (arg.endsWith(".xml")) {
                	    mimeType = "application/xml";
                	} else if (arg.endsWith(".txt")) {
                	    mimeType = "text/plain";
                	} else if (arg.endsWith(".ico")) {
                	    mimeType = "image/x-icon";
                	} else if (arg.endsWith(".svg")) {
                	    mimeType = "image/svg+xml";
                	}

	                 
				     /*****************/
					String sb = "";
					sb = sb+"HTTP/1.0 200 ok\n";
				    sb = sb +"Server: Axel Server/1.0\n";
					sb = sb +"Date: " + new Date()+" \n";
					sb = sb +"Content-Type: " + mimeType + "\n";
					sb = sb +"Content-Length: "+tam_archivo+" \n";
					sb = sb +"\n";
					dos1.write(sb.getBytes());
					dos1.flush();
				     /*****************/
				
	                 while(cont<tam_archivo)
	                 {
	                     x = dis2.read(buf);
	                     dos1.write(buf,0,x);
	                     cont=cont+x;
	                     dos1.flush();
	                    
	                    
	                 }

	                 dis2.close();
	                 dos1.close();
	                     
	                     
				}
				catch(Exception e)
				{
					System.out.println(e.getMessage());
				}
				
			}
			
			public void SendASB(String arg, DataOutputStream dos1) 
			{
                 try{
                	 
				     int b_leidos=0;
	                 DataInputStream dis2 = new DataInputStream(new FileInputStream("/home/nfive-elite/Downloads/"+arg));
	
	                 byte[] buf=new byte[1024];
	                 int x=0;
	                 File ff = new File("/home/nfive-elite/Downloads/"+arg);			
	                 long tam_archivo=ff.length(),cont=0;
	                 
	                 // Determinar el Content-Type basado en la extensión del archivo
	                 String mimeType = "application/octet-stream";  // Predeterminado
	                 if (arg.endsWith(".jpg") || arg.endsWith(".jpeg")) {
	                	    mimeType = "image/jpeg";
                	} else if (arg.endsWith(".png")) {
                	    mimeType = "image/png";
                	} else if (arg.endsWith(".gif")) {
                	    mimeType = "image/gif";
                	} else if (arg.endsWith(".html") || arg.endsWith(".htm")) {
                	    mimeType = "text/html";
                	} else if (arg.endsWith(".css")) {
                	    mimeType = "text/css";
                	} else if (arg.endsWith(".js")) {
                	    mimeType = "application/javascript";
                	} else if (arg.endsWith(".pdf")) {
                	    mimeType = "application/pdf";
                	} else if (arg.endsWith(".mp4")) {
                	    mimeType = "video/mp4";
                	} else if (arg.endsWith(".mp3")) {
                	    mimeType = "audio/mpeg";
                	} else if (arg.endsWith(".wav")) {
                	    mimeType = "audio/wav";
                	} else if (arg.endsWith(".avi")) {
                	    mimeType = "video/x-msvideo";
                	} else if (arg.endsWith(".zip")) {
                	    mimeType = "application/zip";
                	} else if (arg.endsWith(".json")) {
                	    mimeType = "application/json";
                	} else if (arg.endsWith(".xml")) {
                	    mimeType = "application/xml";
                	} else if (arg.endsWith(".txt")) {
                	    mimeType = "text/plain";
                	} else if (arg.endsWith(".ico")) {
                	    mimeType = "image/x-icon";
                	} else if (arg.endsWith(".svg")) {
                	    mimeType = "image/svg+xml";
                	}

	                 
				     /*****************/
					String sb = "";
					sb = sb+"HTTP/1.0 200 ok\n";
				    sb = sb +"Server: Axel Server/1.0\n";
					sb = sb +"Date: " + new Date()+" \n";
					sb = sb +"Content-Type: " + mimeType + "\n";
					sb = sb +"Content-Length: "+tam_archivo+" \n";
					sb = sb +"\n";
					dos1.write(sb.getBytes());
					dos1.flush();
				     /*****************/

	                 dis2.close();
	                 dos1.close();
	                     
	                     
				}
				catch(Exception e)
				{
					System.out.println(e.getMessage());
				}
				
			}
		}
		
		
		public ServidorWeb1() throws Exception
		{
			System.out.println("Iniciando Servidor.......");
			this.ss=new ServerSocket(PUERTO);
			this.ejecutorSubprocesos= Executors.newCachedThreadPool();

			System.out.println("Servidor iniciado:---OK");
			System.out.println("Esperando por Cliente....");
			
			try {
				for(;;)
				{
					Socket accept=ss.accept();
					this.ejecutorSubprocesos.execute(new Manejador(accept));
				}
			}catch(Exception e) {
				this.ejecutorSubprocesos.shutdown();
			}

				
		}
		
		
		
		public static void main(String[] args) throws Exception{
			ServidorWeb1 sWEB=new ServidorWeb1();
		}
	
}
