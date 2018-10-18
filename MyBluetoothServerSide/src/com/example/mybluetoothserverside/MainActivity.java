package com.example.mybluetoothserverside;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.app.Activity;
import android.telephony.PhoneStateListener;
import android.telephony.SmsManager ;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Menu;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.UUID;
import java.util.Iterator;
import java.util.LinkedHashMap;



import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
 
 private static final int REQUEST_ENABLE_BT = 1;
 public static String play_audio = "play";
 
 Intent callIntent;
 
 BluetoothAdapter bluetoothAdapter;
 
 ArrayList<BluetoothDevice> pairedDeviceArrayList;
 
 TextView textInfo, textStatus, textAsync;
 ListView listViewPairedDevice;
 ArrayAdapter<BluetoothDevice> pairedDeviceAdapter;
 private UUID myUUID;
 
 LinearLayout inputPane;
 EditText inputField;
 Button btnSend;
 
 ThreadConnectBTdevice myThreadConnectBTdevice;
 ThreadConnected myThreadConnected;
 
 TelephonyManager myTelephonyManager;
 
 private static int lastState = TelephonyManager.CALL_STATE_IDLE;
 private static Date callStartTime;
 private static boolean isIncoming;
 private static String savedNumber;
 
 public static Map<String, Integer> map = new LinkedHashMap<String, Integer>();
 private AsyncTask<String, Integer, Void> asyncTask;
 private String PREFS_CALL_ACTION = "IntentActionCalledLast";
 
 @Override
 protected void onCreate(Bundle savedInstanceState) {
  super.onCreate(savedInstanceState);
  setContentView(R.layout.activity_main);
  getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

  textInfo = (TextView)findViewById(R.id.info);
  textStatus = (TextView)findViewById(R.id.status);
  textAsync = (TextView)findViewById(R.id.async);
  listViewPairedDevice = (ListView)findViewById(R.id.pairedlist);
  
  inputPane = (LinearLayout)findViewById(R.id.inputpane);
  inputField = (EditText)findViewById(R.id.input);
  
  myTelephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
  
  SharedPreferences setting = getSharedPreferences(PREFS_CALL_ACTION, 0);
  String sharedToast = Integer.toString(setting.getInt("restartFromLastCalled", 0));
  
  Toast.makeText(MainActivity.this, sharedToast, Toast.LENGTH_SHORT).show();
  
  map.put("USERNAME 0",999283277);
  map.put("USERNAME 1",999283277);
  map.put("USERNAME 2",999283277);
  map.put("USERNAME 3",999283277);
  map.put("USERNAME 4",999283277);
  map.put("ELENO JOSE DA SILVA",971123033);
  map.put("MARIANGELA RUSSO",999372088);
  map.put("SONIA VALERIO DA COSTA",992987793);
  map.put("MILTON CUANO",983145716);
  map.put("FRANCISCO MARTINS GALHARDO FILHO",998041684);
  map.put("MARCELO RODRIGUES",999131340);
  map.put("TEOFILO MOTTA OBARA",998399126);
  map.put("CELSO LOURENCO",999302010);
  map.put("DIVA DE MAMAN SPERB",999621952);
  map.put("DENISE CLEMENTINO MORALES",986454420);
  map.put("FLAVIO FAGIONATTO",999953612);
  map.put("RUBENS RUBINI",976269163);
  map.put("JOAO SILVA",981349290);
  map.put("JOELMA DE MELO SOUZA RUSSO",974720687);
  map.put("JAMAL ADNAN NASSER",986112860);
  map.put("ROSA FURIKO CUBOIANA",995796228);
  map.put("LILIAM ELI CARAM",996251526);
  map.put("WELDSON FERREIRRA VITOR",982137520);
  map.put("RAFAEL RENATO DA POLLO",991011450);
  map.put("EDNA MOLEIRO DIANA",998714947);
  map.put("LISETE CIOCCHETTI MACHADO",971198563);
  map.put("NEDA CANDRIA CESAR",972196372);
  map.put("NELINA ALVES MOREIRA",998152100);
  map.put("EDGARD BERNARDO DOS SANTOS",991723616);
  map.put("PAULO SERGIO JOAO",42292294);
  map.put("NELINA ALVES MOREIRA",998152100);
  map.put("JOAO PEREIRA FILHO",996821081);
  map.put("ANTONIO GALELLI FILHO",999388272);
  map.put("FABIO NAZARI DA CUNHA",976004424);
  map.put("MARIA APARECIDA DA SILVA",994448875);
  
  /*
  map.put("USERNAME 1",900000001);
  map.put("USERNAME 2",900000002);
  map.put("USERNAME 3",900000003);
  map.put("USERNAME 4",900000004);
  map.put("bairro Jd Beatriz SBC",43904355);
  map.put("Rua Brasilio Machado, bairro : centro SBC",43397436);
  map.put("Rua Brasilio Machado",43349859);
  map.put("Avenida Robert Kennedy",45940239);
  map.put("unknowed01",0311991810483);
  map.put("unknowed02",0311991811479);
  map.put("unknowed03",0311991811541);
  map.put("unknowed04",0311991810542);
  map.put("unknowed05",0311991811589);
  map.put("unknowed06",0311991810672);
  */
  map.put("end",900000000);
  
  if(sharedToast != "0"){
	  for(Iterator<Entry<String, Integer>> it = map.entrySet().iterator(); it.hasNext(); ) {
	      Entry<String, Integer> entry = it.next();
	      if(entry.getValue().equals(Integer.parseInt(sharedToast))) {
	        it.remove();
	        break;
	      } else {
	    	  it.remove();
	      }
	    }
	  /*
	   for(Map.Entry<String, Integer> entry : map.entrySet()) {
		    final String key = entry.getKey();
		    final Integer value = entry.getValue();
		    if(value.toString() != sharedToast) {
		    	//map.remove(key);
		    } else {
		    	//map.remove(key);
		    	//break;
		    }
	  }
	  */
  }
  
  /*
  map.put("ELENO JOSE DA SILVA",971123033);
  map.put("MARIANGELA RUSSO",999372088);
  map.put("SONIA VALERIO DA COSTA",992987793);
  map.put("MILTON CUANO",983145716);
  map.put("FRANCISCO MARTINS GALHARDO FILHO",998041684);
  map.put("MARCELO RODRIGUES",999131340);
  map.put("TEOFILO MOTTA OBARA",998399126);
  map.put("CELSO LOURENCO",999302010);
  map.put("DIVA DE MAMAN SPERB",999621952);
  map.put("DENISE CLEMENTINO MORALES",986454420);
  map.put("DENISE CLEMENTINO MORALES",986454420);
  map.put("FLAVIO FAGIONATTO",999953612);
  map.put("RUBENS RUBINI",976269163);
  map.put("JOAO SILVA",981349290);
  map.put("JOELMA DE MELO SOUZA RUSSO",974720687);
  map.put("JAMAL ADNAN NASSER",986112860);
  map.put("ROSA FURIKO CUBOIANA",995796228);
  map.put("LILIAM ELI CARAM",996251526);
  map.put("WELDSON FERREIRRA VITOR",982137520);
  map.put("RAFAEL RENATO DA POLLO",991011450);
  map.put("EDNA MOLEIRO DIANA",998714947);
  map.put("LISETE CIOCCHETTI MACHADO",971198563);
  map.put("NEDA CANDRIA CESAR",972196372);
  map.put("NELINA ALVES MOREIRA",998152100);
  map.put("EDGARD BERNARDO DOS SANTOS",991723616);
  map.put("PAULO SERGIO JOAO",42292294);
  map.put("NELINA ALVES MOREIRA",998152100);
  map.put("JOAO PEREIRA FILHO",996821081);
  map.put("ANTONIO GALELLI FILHO",999388272);
  map.put("FABIO NAZARI DA CUNHA",976004424);
  map.put("MARIA APARECIDA DA SILVA",994448875);
  map.put("MARIA TEREZA DE JESUS",965991064);
  map.put("WILZA LENY DE SOUZA",998791063);
  map.put("CLAUDEMIR DELFINO",998460570);
  map.put("DEMETRIO NIFOCI",996092920);
  map.put("WAGNER SOTELO",983524349);
  map.put("CLEYDE DE ANDRADE TOFFOLI",995535377);
  map.put("FERNANDO BATISTA FARIAS",995839737);
  map.put("VICENTE ANTONIO DE SANTIS FARRAFE JUNIOR",982820607);
  map.put("ANTONIO DA SILVA HENRIQUES",989835217);
  map.put("ELIANA APARECIDA PEREIRA",992262167);
  map.put("MARIA NEYLLA LUVIZOTTO MOTTAS",993699636);
  map.put("MARCO ANTONIO GERIN",992522390);
  map.put("HEITOR ATTILIO GANDOLFI",976620118);
  map.put("MARCO ANTONIO SURJAN TROFO",973359694);
  map.put("RUBENS DE SOUZA OLIVEIRA",974195100);
  map.put("ANA PAULA GONCALVES CRUZ RATO",996335363);
  map.put("VALGENI ALVES PAULO",999670279);
  map.put("WANDERLEY APARECIDO DAMASIO",981181596);
  map.put("FERNANDO MARTINS SOARES",999485637);
  map.put("IVONE FRAGA",994739932);
  map.put("VERENICE MEDINA MORALES MANZINI",978529014);
  map.put("SONG KUN KIM",996597100);
  map.put("ANNALI APARECIDA SOBRAL",987545132);
  map.put("AREOLINO PEREIRA DA SILVA NETO",982168857);
  map.put("ANDRE UCHIMURA DE AZEVEDO",978749738);
  map.put("ONESSIMO RODRIGUES JUNIOR",991736429);
  map.put("MARCOS DUARTE AMARAL",983834751);
  map.put("PATRICIA DOS SANTOS",981979604);
  map.put("SUELI MARTINS",995261717);
  map.put("MONICA LANDUCCI BROSSI",996534830);
  map.put("DARLY AUGUSTO",984568502);
  map.put("ABEL VAZ DE PAIVA",997933465);
  map.put("CELSO LOPES",991450375);
  map.put("MARCOS RAGAZZI",991114099);
  map.put("LEONARDO DE ALMEIDA GIESTA",983053273);
  map.put("GENNARO CALIFANO",992929172);
  map.put("SORAIA AFONSO",998229059);
  map.put("MARIA SONIA DA COSTA FRAZAO MATSUO",994901297);
  map.put("LUIS CLAUDIO MARQUES",998309123);
  map.put("CLAUDIO RIBEIRO DOS SANTOS",997085752);
  map.put("MARCOS ANTONIO BUONZO",995492743);
  map.put("JUDITE RODRIGUES TAVARES MENDES",991688290);
  map.put("MARILUCE BATISTA REGULLE",981499586);
  map.put("ANDERSON FONTES ARAUJO",998142609);
  map.put("ROBERTO AKIRA KAWAHARA",999167986);
  map.put("PAULO PESCUMA",973563130);
  map.put("MILTON ARAUJO GONCALVES",998395471);
  map.put("REGINALDO DE SOUZA RODRIGUES",992940292);
  map.put("SANDRA FURLANI SANTOS BAPTISTA",991776939);
  map.put("ROBSON HUMPHREYS",981029477);
  map.put("LIA TERESINHA PRADO",999985640);
  map.put("PATRICIA DOS SANTOS",0);
  map.put("ROSEANE DOS PRAZERES DE OLIVEIRA",978370088);
  map.put("ANA LUCIA PEREIRA",995203893);
  map.put("CARLOS ALBERTO GOMES",995306423);
  map.put("SILVIO LUIZ AMORIM PINTO",992156607);
  map.put("REGINA CELIA DE SOUZA RIO",999512576);
  map.put("ANDREA MARA MATTAR",993569804);
  map.put("EDUARDO AMBROZIO",996319958);
  map.put("DARCIO TADEU BARONE FAZENDA",999048582);
  map.put("CLAUDIO YOSHIRO IANO",996155540);
  map.put("OSVALDO CRITELLI JR",998094179);
  map.put("LUCIMEIRE GONZAGA DE OLIVEIRA EVANGELIST",999308240);
  map.put("CARMENE NORCIA",991778823);
  map.put("NADINE FORCINETTI DE LION",997187073);
  map.put("ELISABETE TELLO",999091964);
  map.put("JOSE CARLOS PERES ARCHONA",999210718);
  map.put("MARTA CASSOLINO",991238557);
  map.put("MARCIO BATISTA EIRAS",991789947);
  map.put("PAULO CEZAR ROCHA PINHEIRO ROSA",999735525);
  map.put("REGINA MARIA MOURA",999522049);
  map.put("SILVIO FRANCISCO NUNES",976815280);
  map.put("GANDI WAGNER DONATO",991680329);
  map.put("SANDRA MORENO MOTA",997524739);
  map.put("GERMANO ANTUNES DE SOUZA FILHO",981031169);
  map.put("LUCIA HELENA TOSTA",995689928);
  map.put("CARLOS FOSSA",999792747);
  map.put("CLEIDE BERTTI",996794277);
  map.put("DANIEL PIERRE DELEU",995080921);
  map.put("GERSON POLIDORO",976533041);
  map.put("NILSON MAZZOLANI",999954744);
  map.put("ANTONIO FERNANDO BOTTEGHIM",996204388);
  map.put("ESTER JULIA DE OLIVEIRA MENDONCA SANTOS",999362352);
  map.put("ANNITA RE FERREIRA",926058145);
  map.put("MARCOS EUGENIO AMADE MAZARIN",981339921);
  map.put("CIBELE VIEIRA SANTOS",920974648);
  map.put("REINALDO CORREA",993414468);
  map.put("JOAO VALDECIR WESSLER",978557524);
  map.put("ROGERIO BARATTI GASPAROTO",998516622);
  map.put("MARY APARECIDA DE SOUZA MOTA",972061531);
  map.put("MARCOS NASSIB MASSATO SHIMADA",992225695);
  map.put("NILSON CARVALHO",999027295);
  map.put("MARIA FERNANDA CRISTOVAO",997608259);
  map.put("TANIA LEIVA SANCHEZ",981821686);
  map.put("BEATRIZ PEREIRA",992044989);
  map.put("BEATRIZ PEREIRA",992044989);
  map.put("MAURICIO RIBEIRO",930317937);
  map.put("MARCOS BRAGHITTONI",997989871);
  map.put("JOSE ANTONIO CAMPOS",995955329);
  map.put("SUZANA PILAR BARREIRO JAMARDO",991714726);
  map.put("SUZANA PILAR BARREIRO JAMARDO",991714726);
  map.put("MARIA INEZ SOARES ,",997490883);
  map.put("MARCO ANTONIO DOS SANTOS",966404638);
  map.put("ROBINSON VIVI",981412347);
  map.put("RICARDO SANTA MARIA MARINS",982654646);
  map.put("DUZOLINA HELENA LAHR",982567271);
  map.put("JOSE CICERO VANDERLEI",996936614);
  map.put("MAURICELIA DE MELO",991910368);
  map.put("JAIR RODRIGUES LIMA",999790080);
  map.put("AYRES RAMOS PLATA",998929838);
  map.put("RICARDO MELO DIB",999038216);
  map.put("DECIO ROBERTO CRESCENTE",997607635);
  map.put("REGINA CELIA SPINA DINIZ",991264898);
  map.put("MIRIAM VIEIRA ISSA",996911240);
  map.put("RAFAEL SANCHES FILHO",976371608);
  map.put("VALERIA SALIN",991979931);
  map.put("MARIA CRISTINA LOPES",996377160);
  map.put("JOAO GUIDOLIN",999141217);
  map.put("MOHAMED SUAILI",982020123);
  map.put("PAULO CESAR ESTEVES",981938021);
  map.put("BENIVALDO DOS SANTOS DE ALMEIDA",972830584);
  map.put("FATIMA DE SOUZA",999413236);
  map.put("CESAR AUGUSTO RAYMUNDO",999052950);
  map.put("JOSE CARLOS VIEIRA",995372942);
  map.put("PAULO SERGIO JOAO",42292294);
  */
  
  btnSend = (Button)findViewById(R.id.send);
  btnSend.setOnClickListener(new OnClickListener(){

   @Override
   public void onClick(View v) {
	   asyncTask = new outgoingCallAsyncTask();
	   //outgoingCallAsyncTask asyncTask = new outgoingCallAsyncTask();
	   asyncTask.execute();
	   /*
	   if(myThreadConnected!=null){
		   byte[] bytesToSend = play_audio.getBytes();
		   myThreadConnected.write(bytesToSend);
		   Intent callIntent = new Intent(Intent.ACTION_CALL);
		   String dialPhoneNumber = inputField.getText().toString(); 
		   callIntent.setData(Uri.parse("tel:"+dialPhoneNumber));
		   startActivity(callIntent);
	   }
	   */
	   /*
	   for(Map.Entry<String, Integer> entry : map.entrySet()) {
		    String key = entry.getKey();
		    Integer value = entry.getValue();
		
		    Intent callIntent = new Intent(Intent.ACTION_CALL);
		 
			String dialPhoneNumber = value.toString();
			callIntent.setData(Uri.parse("tel:"+dialPhoneNumber));
			startActivity(callIntent);
			Integer timeInMills = 10000;
			SystemClock.sleep(timeInMills);
		}
		*/
	   /*
	   Intent callIntent = new Intent(Intent.ACTION_CALL);
	   String dialPhoneNumber = inputField.getText().toString();
	   callIntent.setData(Uri.parse("tel:"+dialPhoneNumber));
	   startActivity(callIntent);
	   */
   }});
  
  if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH)){
   Toast.makeText(this, 
    "FEATURE_BLUETOOTH NOT support", 
    Toast.LENGTH_LONG).show();
            finish();
            return;
  }

  //generate UUID on web: http://www.famkruithof.net/uuid/uuidgen
  //have to match the UUID on the another device of the BT connection
  myUUID = UUID.fromString("ec79da00-853f-11e4-b4a9-0800200c9a66");
  
  bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
  if (bluetoothAdapter == null) {
   Toast.makeText(this, 
    "Bluetooth is not supported on this hardware platform", 
    Toast.LENGTH_LONG).show();
            finish();
            return;
  }
  
  String stInfo = bluetoothAdapter.getName() + "\n" +
    bluetoothAdapter.getAddress();
  textInfo.setText(stInfo);
  
  /*
   * Broadcast receiver is registered in AndroidManifet tag <receiver>
   * or programatically as below registerReceiver()
   */
  
  IntentFilter intentFilter = new IntentFilter(Intent.ACTION_NEW_OUTGOING_CALL);
  OutgoingReceiver outgoingReceiver = new OutgoingReceiver();
  registerReceiver(outgoingReceiver, intentFilter);
  
  /*
   * A phoneStateListener não se usa instanciando new ...()
   * https://www.codeproject.com/Articles/548416/Detecting-Incoming-and-Outgoing-Phone-Calls-on-And  download source code 405.3 KB
   * codeproject.com says to use a broadcast as a receiver in manifest file
   * use the phoneStateListener inside method under BoadcastReceviver
   * 
   */
  
  PhoneStateListener callStateListener = new MyPhoneStateListener();
  myTelephonyManager = (TelephonyManager)this.getSystemService(Context.TELEPHONY_SERVICE);
  myTelephonyManager.listen(callStateListener, PhoneStateListener.LISTEN_CALL_STATE);

  
 }

 @Override
 protected void onStart() {
  super.onStart();
  
  //Turn ON BlueTooth if it is OFF
  if (!bluetoothAdapter.isEnabled()) {
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
        }
  
  setup();
 }
 
 private void setup() {
  Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();
  if (pairedDevices.size() > 0) {
   pairedDeviceArrayList = new ArrayList<BluetoothDevice>();
   
   for (BluetoothDevice device : pairedDevices) {
    pairedDeviceArrayList.add(device);
            }
   
   pairedDeviceAdapter = new ArrayAdapter<BluetoothDevice>(this,
                 android.R.layout.simple_list_item_1, pairedDeviceArrayList);
   listViewPairedDevice.setAdapter(pairedDeviceAdapter);
   
   listViewPairedDevice.setOnItemClickListener(new OnItemClickListener(){

    @Override
    public void onItemClick(AdapterView<?> parent, View view,
      int position, long id) {
     BluetoothDevice device = 
       (BluetoothDevice)parent.getItemAtPosition(position);
      Toast.makeText(MainActivity.this, 
       "Name: " + device.getName() + "\n"
       + "Address: " + device.getAddress() + "\n"
       + "BondState: " + device.getBondState() + "\n"
       + "BluetoothClass: " + device.getBluetoothClass() + "\n"
       + "Class: " + device.getClass(),
       Toast.LENGTH_LONG).show();
      
      textStatus.setText("start ThreadConnectBTdevice");
      myThreadConnectBTdevice = new ThreadConnectBTdevice(device);
      myThreadConnectBTdevice.start();
    }});
  }
 }

 @Override
 protected void onDestroy() {
  super.onDestroy();
  
  if(myThreadConnectBTdevice!=null){
   myThreadConnectBTdevice.cancel();
  }
 }

 @Override
 protected void onActivityResult(int requestCode, int resultCode, Intent data) {
  
  if(requestCode==REQUEST_ENABLE_BT){
   if(resultCode == Activity.RESULT_OK){
    setup();
   }else{
    Toast.makeText(this, 
     "BlueTooth NOT enabled", 
     Toast.LENGTH_SHORT).show();
             finish();
   }
  } 
 }
 
 private class ThreadConnectBTdevice extends Thread {
  private BluetoothSocket bluetoothSocket = null;
        private final BluetoothDevice bluetoothDevice;
        
        
        public ThreadConnectBTdevice(BluetoothDevice device) {
         bluetoothDevice = device;

         try {
    bluetoothSocket = device.createRfcommSocketToServiceRecord(myUUID);
    textStatus.setText("bluetoothSocket: \n" + bluetoothSocket);
   } catch (IOException e) {
    // TODO Auto-generated catch block
    e.printStackTrace();
   }
        }

  @Override
  public void run() {
   boolean success = false;
   try {
    bluetoothSocket.connect();
    success = true;
   } catch (IOException e) {
    e.printStackTrace();
    
    final String eMessage = e.getMessage();
    runOnUiThread(new Runnable(){

     @Override
     public void run() {
      textStatus.setText("something wrong bluetoothSocket.connect(): \n" + eMessage);
     }});
    
    try {
     bluetoothSocket.close();
    } catch (IOException e1) {
     // TODO Auto-generated catch block
     e1.printStackTrace();
    }
   }
   
   if(success){
    //connect successful
    final String msgconnected = "connect successful:\n"
     + "BluetoothSocket: " + bluetoothSocket + "\n"
     + "BluetoothDevice: " + bluetoothDevice;
    
    runOnUiThread(new Runnable(){

     @Override
     public void run() {
      textStatus.setText(msgconnected);
      
      listViewPairedDevice.setVisibility(View.GONE);
      inputPane.setVisibility(View.VISIBLE);
     }});
    
    startThreadConnected(bluetoothSocket);
   }else{
    //fail
   }
  }
  
  public void cancel() {
   
   Toast.makeText(getApplicationContext(), 
     "close bluetoothSocket", 
     Toast.LENGTH_LONG).show();
   
   try {
    bluetoothSocket.close();
   } catch (IOException e) {
    // TODO Auto-generated catch block
    e.printStackTrace();
   }
   
        }
 }
 
 private void startThreadConnected(BluetoothSocket socket){
  
  myThreadConnected = new ThreadConnected(socket);
  myThreadConnected.start();
 }
 
 private class ThreadConnected extends Thread {
  private final BluetoothSocket connectedBluetoothSocket;
        private final InputStream connectedInputStream;
        private final OutputStream connectedOutputStream;
  
  public ThreadConnected(BluetoothSocket socket) {
   connectedBluetoothSocket = socket;
   InputStream in = null;
            OutputStream out = null;
            
            try {
    in = socket.getInputStream();
    out = socket.getOutputStream();
   } catch (IOException e) {
    // TODO Auto-generated catch block
    e.printStackTrace();
   }
            
            connectedInputStream = in;
            connectedOutputStream = out;
  }

  @Override
  public void run() {
   byte[] buffer = new byte[1024];
            int bytes;
            
            while (true) {
             try {
     bytes = connectedInputStream.read(buffer);
     String strReceived = new String(buffer, 0, bytes);
     final String msgReceived = String.valueOf(bytes) + 
       " bytes received:\n" 
       + strReceived;
     
     runOnUiThread(new Runnable(){

      @Override
      public void run() {
       textStatus.setText(msgReceived);
      }});
     
     if(1==1){
    	 asyncTask.cancel(true);
     }
    } catch (IOException e) {
     // TODO Auto-generated catch block
     e.printStackTrace();
     
     final String msgConnectionLost = "Connection lost:\n"
       + e.getMessage();
     runOnUiThread(new Runnable(){

      @Override
      public void run() {
       textStatus.setText(msgConnectionLost);
      }});
    }
            }
  }
  
  public void write(byte[] buffer) {
   try {
    connectedOutputStream.write(buffer);
   } catch (IOException e) {
    // TODO Auto-generated catch block
    e.printStackTrace();
   }
        }
  
  public void cancel() {
   try {
    connectedBluetoothSocket.close();
   } catch (IOException e) {
    // TODO Auto-generated catch block
    e.printStackTrace();
   }
  }

 }
 
 	/*
 	public class OutgoingReceiver extends BroadcastReceiver {

 		TelephonyManager myTelephoneManager;
		@Override
		public void onReceive(Context arg0, Intent arg1) {
			if (arg1.getAction().equals("android.intent.action.NEW_OUTGOING_CALL")) {
				myTelephoneManager = (TelephonyManager) arg0.getSystemService(Context.TELEPHONY_SERVICE);
				String state = arg1.getStringExtra(TelephonyManager.EXTRA_STATE);
				String number=arg1.getStringExtra(Intent.EXTRA_PHONE_NUMBER);
				if(myThreadConnected!=null){
				   byte[] bytesToSend = play_audio.getBytes();
				   myThreadConnected.write(bytesToSend);
				}
				Toast.makeText(arg0, state+" - "+number, Toast.LENGTH_LONG).show();
			}
			//new MyPhoneStateListener();
		}
 		
 	}
 	*/
 	
 /*
 * registered in AndroidManifest BroadcastReceiver
 */
 
 public class OutgoingReceiver extends BroadcastReceiver {
	 
	    @Override
	    public void onReceive(Context context, Intent intent) {
	        //Log.w("intent " , intent.getAction().toString());
	 
	        if (intent.getAction().equals("android.intent.action.NEW_OUTGOING_CALL")) {
	            savedNumber = intent.getExtras().getString("android.intent.extra.PHONE_NUMBER");
	            if(myThreadConnected!=null){
	            	/*
	            	byte[] bytesToSend = play_audio.getBytes();
	            	myThreadConnected.write(bytesToSend);
	            	*/
				}
	        }
	        else {
	            String stateStr = intent.getExtras().getString(TelephonyManager.EXTRA_STATE);
	            String number = intent.getExtras().getString(TelephonyManager.EXTRA_INCOMING_NUMBER);
	            int state = 0;
	            if(stateStr.equals(TelephonyManager.EXTRA_STATE_IDLE)){
	                state = TelephonyManager.CALL_STATE_IDLE;
	            }
	            else if(stateStr.equals(TelephonyManager.EXTRA_STATE_OFFHOOK)){
	                state = TelephonyManager.CALL_STATE_OFFHOOK;
	            }
	            else if(stateStr.equals(TelephonyManager.EXTRA_STATE_RINGING)){
	                state = TelephonyManager.CALL_STATE_RINGING;
	                //Toast.makeText(getApplicationContext(), "PHONE STATE RINGING detected", Toast.LENGTH_LONG).show();
	                /*
	                if(myThreadConnected!=null){
	 				   byte[] bytesToSend = play_audio.getBytes();
	 				   myThreadConnected.write(bytesToSend);
	 				}
	 				*/
	            }
	 
	            onCallStateChanged(context, state, number);
	        }
	    }
	 
	 
	    public void onCallStateChanged(Context context, int state, String number) {
	        if(lastState == state){
	            //No change, debounce extras
	            return;
	        }
	        switch (state) {
	            case TelephonyManager.CALL_STATE_RINGING:
	                isIncoming = true;
	                callStartTime = new Date();
	                savedNumber = number;
	 
	                Toast.makeText(context, "Incoming Call Ringing" , Toast.LENGTH_SHORT).show();
	                break;
	            case TelephonyManager.CALL_STATE_OFFHOOK:
	                //Transition of ringing->offhook are pickups of incoming calls.  Nothing done on them
	                if(lastState != TelephonyManager.CALL_STATE_RINGING){
	                    isIncoming = false;
	                    callStartTime = new Date();
	                    /*
	                    byte[] bytesToSend = play_audio.getBytes();
	 				   	myThreadConnected.write(bytesToSend);
	                    Toast.makeText(context, "Outgoing Call Started" , Toast.LENGTH_SHORT).show();
	                    */	                    
	                }
	 
	                break;
	            case TelephonyManager.CALL_STATE_IDLE:
	                //Went to idle-  this is the end of a call.  What type depends on previous state(s)
	                if(lastState == TelephonyManager.CALL_STATE_RINGING){
	                    //Ring but no pickup-  a miss
	                    Toast.makeText(context, "Ringing but no pickup" + savedNumber + " Call time " + callStartTime +" Date " + new Date() , Toast.LENGTH_SHORT).show();
	                }
	                else if(isIncoming){
	 
	                    Toast.makeText(context, "Incoming " + savedNumber + " Call time " + callStartTime  , Toast.LENGTH_SHORT).show();
	                }
	                else{
	     				
	                    Toast.makeText(context, "outgoing " + savedNumber + " Call time " + callStartTime +" Date " + new Date() , Toast.LENGTH_SHORT).show();
	 
	                }
	 
	                break;
	        }
	        lastState = state;
	    }
	}
 
 	protected class MyPhoneStateListener extends PhoneStateListener {
 		
 		MyPhoneStateListener(){
 			
 		}
 		
 	    @Override
 	    public void onCallStateChanged(int state, String incomingNumber) {
 	        switch (state){
	            case TelephonyManager.CALL_STATE_RINGING:
	               // do whatever you want here
	            	
	            	// http://androideasylessons.blogspot.com.br/2012/09/answer-incoming-call-in-android.html
	            	
	            	Intent answer = new Intent(Intent.ACTION_MEDIA_BUTTON);
                    answer.putExtra(Intent.EXTRA_KEY_EVENT, new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_HEADSETHOOK));
                    sendOrderedBroadcast(answer, null);
	                break;
	            case TelephonyManager.CALL_STATE_OFFHOOK:
	                // do whatever you want here
	            	/*
	            	if(myThreadConnected!=null){
					   byte[] bytesToSend = play_audio.getBytes();
					   myThreadConnected.write(bytesToSend);
					}
					*/
	                break;
	            case TelephonyManager.CALL_STATE_IDLE:
	                // do whatever you want here
 	        }
 	    }
 	}
 	
 	public class outgoingCallAsyncTask extends AsyncTask<String,Integer,Void>{

		@Override
		protected Void doInBackground(String... arg0) {
			 
			for(Map.Entry<String, Integer> entry : map.entrySet()) {
			    final String key = entry.getKey();
			    final Integer value = entry.getValue();		
			    
			    Intent callIntent = new Intent(Intent.ACTION_CALL);
			 
			    String dialPhoneNumber = value.toString();
				callIntent.setData(Uri.parse("tel:"+dialPhoneNumber));
				startActivity(callIntent);
				
				/*
				 * 
				 * 
				 */
			    
			    SharedPreferences setting = getSharedPreferences(PREFS_CALL_ACTION, 0);
			    
			    // apagar as 2 linhas e usa o broadcastreceiver pra mandar o play
			    //to usando esse aqui quando não manda argumento ACTION_CALL			   
			    
			    byte[] bytesToSend = play_audio.getBytes();
				myThreadConnected.write(bytesToSend);
				
				runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                    	textAsync.setText(key+" cel.: "+value.toString());
                    }
                });
				// espera 10s para enviar o play, senão vai 2x playplay por causa dos map.put() map.put()
				/*
				Integer timeInMills = 10000;
				*/
				// wait 20 seconds to next for loop iteration
				
				Integer timeInMills = 25000;
				//Integer timeInMills = 45000;
				//SystemClock.sleep(timeInMills);
				try {
					Thread.sleep(timeInMills);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				SharedPreferences.Editor editor = setting.edit();
				editor.putInt("restartFromLastCalled", value);
			    editor.commit();
				
				if (isCancelled()) break;
				
				
				TelephonyManager tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
				Method m1 = null, m2 = null, m3 = null;
				try {
					m1 = tm.getClass().getDeclaredMethod("getITelephony");
				} catch (NoSuchMethodException e) {
					e.printStackTrace();
				}
				m1.setAccessible(true);
				try {
					Object iTelephony = m1.invoke(tm);
					m2 = iTelephony.getClass().getDeclaredMethod("silenceRinger");
					m2.invoke(iTelephony);
					m3 = iTelephony.getClass().getDeclaredMethod("endCall"); 				
					m3.invoke(iTelephony);
				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (NoSuchMethodException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				
				Integer timeInMillsForNextLoop = 20000;
				//SystemClock.sleep(timeInMills);
				try {
					Thread.sleep(timeInMillsForNextLoop);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				//sendSMS(value.toString());
				
			}
			return null;
		}
 		
 	}
 	
 	protected void sendSMS(final String dialPhoneNumber) {
 	      Log.i("Send SMS", "");
 	      
 	      Intent smsIntent = new Intent(Intent.ACTION_VIEW);
 	      
 	      smsIntent.setData(Uri.parse("smsto:"));
 	      smsIntent.setType("vnd.android-dir/mms-sms");
 	      smsIntent.putExtra("address"  , new String (dialPhoneNumber));
 	      smsIntent.putExtra("sms_body"  , "Teste - venda apartamento na planta 19 a 45 metros quadrados");
 	      
 	     runOnUiThread(new Runnable() {
	           @Override
	           public void run() {
	        	   Toast.makeText(getApplicationContext(), "SMS sent."+ dialPhoneNumber, 
	                       Toast.LENGTH_LONG).show();
	           }
	     });
 	      try {
 	         startActivity(smsIntent);
 	         finish();
 	         Log.i("Finished sending SMS...", "");
 	      } catch (Exception ex) {
 	    	  
 	    	 runOnUiThread(new Runnable() {
		           @Override
		           public void run() {
		        	   Toast.makeText(MainActivity.this, 
		        	 	         "SMS faild, please try again later.", Toast.LENGTH_SHORT).show();
		           }
		       });
 	         ex.printStackTrace();
 	      }
 	   }
 	 
}