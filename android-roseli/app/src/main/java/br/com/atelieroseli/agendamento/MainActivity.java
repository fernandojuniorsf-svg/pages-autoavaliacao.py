package br.com.atelieroseli.agendamento;
import android.app.*;
import android.os.*;
import android.content.*;
import android.graphics.*;
import android.graphics.drawable.*;
import android.view.*;
import android.widget.*;
import java.util.*;
public class MainActivity extends Activity {
 int wine=Color.rgb(64,10,30),gold=Color.rgb(202,169,104),ink=Color.rgb(51,33,40),paper=Color.rgb(250,246,241),step=0;
 LinearLayout main,body; String service="",mode="No ateliê",deadline="Normal",day="",hour="",pickup="",returnAddr="",person="",phone="",notes="",itemValue=""; boolean insure=false;
 EditText ePickup,eReturn,ePerson,ePhone,eNotes,eValue;
 int dp(int n){return (int)(n*getResources().getDisplayMetrics().density+.5f);}
 GradientDrawable bg(int color){GradientDrawable g=new GradientDrawable();g.setColor(color);g.setCornerRadius(dp(14));return g;}
 TextView text(String s,int size,boolean bold,int color){TextView t=new TextView(this);t.setText(s);t.setTextColor(color);t.setTextSize(size);if(bold)t.setTypeface(Typeface.DEFAULT,Typeface.BOLD);t.setPadding(0,dp(6),0,dp(8));body.addView(t);return t;}
 Button button(String s,boolean dark){Button b=new Button(this);b.setAllCaps(false);b.setText(s);b.setTextColor(dark?-1:wine);b.setBackground(bg(dark?wine:0xffeae2d9));LinearLayout.LayoutParams p=new LinearLayout.LayoutParams(-1,dp(52));p.topMargin=dp(10);body.addView(b,p);return b;}
 EditText field(String hint,String value,int type){EditText e=new EditText(this);e.setText(value);e.setHint(hint);e.setTextSize(15);e.setSingleLine(false);e.setInputType(type);e.setPadding(dp(12),dp(8),dp(12),dp(8));e.setBackground(bg(-1));LinearLayout.LayoutParams p=new LinearLayout.LayoutParams(-1,dp(52));p.topMargin=dp(8);body.addView(e,p);return e;}
 void toast(String s){Toast.makeText(this,s,Toast.LENGTH_LONG).show();}
 void title(String a,String b){text(a,25,true,wine);text(b,14,false,0xff736870);}
 void next(){step++;draw();}
 void previous(){step--;draw();}
 @Override public void onCreate(Bundle b){super.onCreate(b);getWindow().setStatusBarColor(wine);getWindow().setNavigationBarColor(paper);draw();}
 void draw(){
  main=new LinearLayout(this);main.setOrientation(1);main.setBackgroundColor(paper);setContentView(main);
  TextView h=new TextView(this);h.setText("AR    ATELIÊ ROSELI\n        CONSERTOS & COSTURAS");h.setTypeface(Typeface.SERIF,Typeface.BOLD);h.setTextSize(17);h.setTextColor(gold);h.setBackgroundColor(wine);h.setGravity(Gravity.CENTER_VERTICAL);h.setPadding(dp(20),0,0,0);main.addView(h,new LinearLayout.LayoutParams(-1,dp(80)));
  ScrollView sc=new ScrollView(this);main.addView(sc);body=new LinearLayout(this);body.setOrientation(1);body.setPadding(dp(22),dp(24),dp(22),dp(45));sc.addView(body);text("AGENDAMENTO · ETAPA "+(step+1)+" DE 4",12,true,0xffa27f43);
  if(step==0)servicePage();else if(step==1)transportPage();else if(step==2)datePage();else confirmPage();
 }
 void servicePage(){
  title("O que precisa ajustar?","Escolha o serviço e depois informe sua preferência de atendimento.");
  String[] options={"Barra de calça","Vestido de festa","Troca de zíper","Ajuste de cintura","Consertos em geral","Outro serviço"};
  for(String s:options){Button b=button((s.equals(service)?"✓ ":"○ ")+s,s.equals(service));b.setOnClickListener(v->{service=s;draw();});}
  button("Continuar →",true).setOnClickListener(v->{if(service.isEmpty())toast("Escolha o serviço.");else next();});
 }
 void transportPage(){
  title("Coleta e entrega","Escolha como a peça vai chegar ao ateliê e como será devolvida.");
  for(String s:new String[]{"No ateliê","Coleta","Devolução","Coleta + devolução"}){Button b=button((s.equals(mode)?"✓ ":"○ ")+s,s.equals(mode));b.setOnClickListener(v->{mode=s;draw();});}
  text("Prazo desejado",15,true,wine);
  Spinner spinner=new Spinner(this);String[] deadlines={"Normal","Expresso: 2 horas (sujeito a confirmação)","24 horas (sujeito a confirmação)"};
  spinner.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,deadlines));spinner.setSelection(deadline.startsWith("Expresso")?1:deadline.startsWith("24")?2:0);body.addView(spinner);
  spinner.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener(){public void onNothingSelected(android.widget.AdapterView<?> a){}public void onItemSelected(android.widget.AdapterView<?> a,View v,int p,long id){deadline=deadlines[p];}});
  if(mode.contains("Coleta")){text("Endereço para coleta",14,true,wine);ePickup=field("Rua, número, CEP, cidade",pickup,1);}else ePickup=null;
  if(mode.contains("Devolução")||mode.contains("devolução")){text("Endereço para devolução",14,true,wine);eReturn=field("Rua, número, CEP, cidade",returnAddr,1);}else eReturn=null;
  text("Cotação do transporte",17,true,wine);
  text("O frete de cada trecho será confirmado pelo ateliê após cotação na Uber Direct. Não calculamos tarifa oficial por km sem acesso à API.",13,false,ink);
  CheckBox cb=new CheckBox(this);cb.setText("Desejo verificar seguro opcional da encomenda");cb.setChecked(insure);body.addView(cb);
  text("Valor declarado da peça (opcional)",14,true,wine);eValue=field("R$ 0,00",itemValue,1);
  button("Continuar →",true).setOnClickListener(v->{if(ePickup!=null)pickup=ePickup.getText().toString().trim();if(eReturn!=null)returnAddr=eReturn.getText().toString().trim();insure=cb.isChecked();itemValue=eValue.getText().toString().trim();
    if(ePickup!=null&&pickup.length()<8){toast("Digite o endereço para coleta.");return;}
    if(eReturn!=null&&returnAddr.length()<8){toast("Digite o endereço para devolução.");return;}next();});
  button("← Voltar",false).setOnClickListener(v->previous());
 }
 void datePage(){
  title("Escolha sua data","A Roseli confirmará a disponibilidade antes da reserva.");
  button(day.isEmpty()?"Selecionar data":day,false).setOnClickListener(v->{Calendar c=Calendar.getInstance();DatePickerDialog d=new DatePickerDialog(this,(p,y,m,dd)->{day=String.format(Locale.ROOT,"%02d/%02d/%04d",dd,m+1,y);draw();},c.get(Calendar.YEAR),c.get(Calendar.MONTH),c.get(Calendar.DAY_OF_MONTH));d.getDatePicker().setMinDate(System.currentTimeMillis()-1000);d.show();});
  button(hour.isEmpty()?"Selecionar horário":hour,false).setOnClickListener(v->new TimePickerDialog(this,(p,h,m)->{hour=String.format(Locale.ROOT,"%02d:%02d",h,m);draw();},9,0,true).show());
  button("Continuar →",true).setOnClickListener(v->{if(day.isEmpty()||hour.isEmpty())toast("Informe data e horário.");else next();});
  button("← Voltar",false).setOnClickListener(v->previous());
 }
 void confirmPage(){
  title("Últimos detalhes","Revise o pedido e envie pelo WhatsApp ou outro app.");
  text("Seu nome",14,true,wine);ePerson=field("Nome completo",person,1);
  text("WhatsApp",14,true,wine);ePhone=field("DDD + número",phone,3);
  text("Observações",14,true,wine);eNotes=field("Peça, tecido, quantidade e cuidados",notes,1);
  text("RESUMO",12,true,0xffa27f43);
  text(service+"\n"+mode+"\n"+day+" às "+hour+"\n"+deadline+"\nColeta: "+pickup+"\nDevolução: "+returnAddr+"\nFrete e seguro: a confirmar",14,false,ink);
  button("Compartilhar solicitação",true).setOnClickListener(v->{
    person=ePerson.getText().toString().trim();phone=ePhone.getText().toString().trim();notes=eNotes.getText().toString().trim();
    if(person.length()<3||phone.replaceAll("\\D","").length()<10){toast("Informe nome e WhatsApp válidos.");return;}
    String msg="Olá, Roseli! Quero solicitar um agendamento.\n\nCliente: "+person+"\nWhatsApp: "+phone+"\nServiço: "+service+"\nAtendimento: "+mode+"\nPrazo: "+deadline+"\nData: "+day+" às "+hour+"\nColeta: "+pickup+"\nDevolução: "+returnAddr+"\nSeguro opcional: "+(insure?"Consultar":"Não solicitado")+"\nValor declarado: "+itemValue+"\nObservações: "+notes+"\n\nAguardo confirmação de disponibilidade e orçamento de coleta/devolução e seguro.";
    Intent i=new Intent(Intent.ACTION_SEND);i.setType("text/plain");i.putExtra(Intent.EXTRA_TEXT,msg);startActivity(Intent.createChooser(i,"Enviar pedido ao Ateliê Roseli"));
  });
  button("← Voltar",false).setOnClickListener(v->previous());
  text("Importante: o pedido não fica agendado até a confirmação da Roseli.",12,false,0xff74666b);
 }
}