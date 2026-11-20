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
 GradientDrawable bg(int color){GradientDrawable g=new GradientDrawable();g.setColor(color);g.setCornerRadius(dp(19));return g;}
 GradientDrawable outline(int color,int stroke){GradientDrawable g=bg(color);g.setStroke(dp(1),stroke);return g;}
 TextView text(String s,int size,boolean bold,int color){TextView t=new TextView(this);t.setText(s);t.setTextColor(color);t.setTextSize(size);t.setTypeface(Typeface.create(bold?"sans-serif-medium":"sans-serif",bold?Typeface.BOLD:Typeface.NORMAL));t.setLineSpacing(dp(2),1.06f);t.setPadding(0,dp(5),0,dp(9));body.addView(t);return t;}
 Button button(String s,boolean dark){Button b=new Button(this);b.setAllCaps(false);b.setText(s);b.setTextSize(15);b.setTypeface(Typeface.create("sans-serif-medium",Typeface.BOLD));b.setTextColor(dark?Color.WHITE:wine);b.setBackground(outline(dark?wine:Color.WHITE,dark?wine:0xffe4d9cf));b.setElevation(dp(dark?3:0));b.setPadding(dp(15),0,dp(15),0);LinearLayout.LayoutParams p=new LinearLayout.LayoutParams(-1,dp(56));p.topMargin=dp(11);body.addView(b,p);return b;}
 EditText field(String hint,String value,int type){EditText e=new EditText(this);e.setText(value);e.setHint(hint);e.setTextColor(ink);e.setHintTextColor(0xff95878b);e.setTextSize(15);e.setSingleLine(false);e.setInputType(type);e.setPadding(dp(16),dp(8),dp(16),dp(8));e.setBackground(outline(Color.WHITE,0xffe6d9ce));LinearLayout.LayoutParams p=new LinearLayout.LayoutParams(-1,dp(54));p.topMargin=dp(9);body.addView(e,p);return e;}
 void toast(String s){Toast.makeText(this,s,Toast.LENGTH_LONG).show();}
 void title(String a,String b){text(a,29,true,wine);TextView sub=text(b,14,false,0xff796f74);sub.setPadding(0,dp(7),0,dp(22));}
 void next(){step++;draw();}
 void previous(){step--;draw();}
 @Override public void onCreate(Bundle b){super.onCreate(b);getWindow().setStatusBarColor(wine);getWindow().setNavigationBarColor(paper);draw();}
 void draw(){
  main=new LinearLayout(this);main.setOrientation(1);main.setBackgroundColor(paper);setContentView(main);
  LinearLayout header=new LinearLayout(this);header.setOrientation(LinearLayout.HORIZONTAL);header.setGravity(Gravity.CENTER_VERTICAL);header.setPadding(dp(18),dp(7),dp(12),dp(7));header.setBackgroundColor(wine);
  main.addView(header,new LinearLayout.LayoutParams(-1,dp(90)));
  ImageView logo=new ImageView(this);logo.setImageResource(br.com.atelieroseli.agendamento.R.drawable.monograma_ar_original);logo.setScaleType(ImageView.ScaleType.FIT_CENTER);
  header.addView(logo,new LinearLayout.LayoutParams(dp(79),dp(75)));
  LinearLayout names=new LinearLayout(this);names.setOrientation(LinearLayout.VERTICAL);names.setPadding(dp(2),0,0,0);header.addView(names,new LinearLayout.LayoutParams(0,-2,1f));
  TextView name=new TextView(this);name.setText("ATELIÊ ROSELI");name.setTextColor(0xffe9c78d);name.setTextSize(20);name.setTypeface(Typeface.create("serif",Typeface.BOLD));names.addView(name);
  TextView sub=new TextView(this);sub.setText("C O N S E R T O S  &  C O S T U R A S");sub.setTextColor(0xffcda975);sub.setTextSize(8);sub.setTypeface(Typeface.create("sans-serif-medium",Typeface.NORMAL));names.addView(sub);
  ScrollView sc=new ScrollView(this);sc.setFillViewport(true);main.addView(sc,new LinearLayout.LayoutParams(-1,0,1));
  body=new LinearLayout(this);body.setOrientation(1);body.setPadding(dp(22),dp(28),dp(22),dp(42));sc.addView(body);
  TextView eyebrow=text("AGENDAMENTO  /  "+new String[]{"SERVIÇO","TRANSPORTE","DATA E HORA","CONFIRMAÇÃO"}[step],11,true,0xffa27f43);
  eyebrow.setLetterSpacing(.1f);
  LinearLayout progress=new LinearLayout(this);progress.setOrientation(LinearLayout.HORIZONTAL);body.addView(progress);
  for(int i=0;i<4;i++){View segment=new View(this);segment.setBackground(bg(i<=step?wine:0xffe8ded5));LinearLayout.LayoutParams pp=new LinearLayout.LayoutParams(0,dp(4),1);pp.setMargins(dp(i==0?0:4),dp(3),0,dp(21));progress.addView(segment,pp);}
  if(step==0)servicePage();else if(step==1)transportPage();else if(step==2)datePage();else confirmPage();
 }
 void servicePage(){
  title("Vamos cuidar da sua peça?","Selecione o serviço. Você poderá informar detalhes antes de enviar seu pedido.");
  String[] options={"Barra de calça","Vestido de festa","Troca de zíper","Ajuste de cintura","Consertos em geral","Outro serviço"};
  text("NOSSOS SERVIÇOS",11,true,0xff967c5b);
  for(String s:options){Button b=button((s.equals(service)?"✓ ":"○ ")+s,s.equals(service));b.setOnClickListener(v->{service=s;draw();});}
  text("✧ Cada peça merece atenção especial.",13,false,0xff81716c);
  button("Continuar  →",true).setOnClickListener(v->{if(service.isEmpty())toast("Escolha o serviço.");else next();});
 }
 void transportPage(){
  title("Como prefere o atendimento?","Você pode trazer sua peça ou solicitar coleta e devolução.");
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
  title("Quando fica melhor?","Escolha a data e o horário desejados. Roseli confirma a disponibilidade.");
  button(day.isEmpty()?"Selecionar data":day,false).setOnClickListener(v->{Calendar c=Calendar.getInstance();DatePickerDialog d=new DatePickerDialog(this,(p,y,m,dd)->{day=String.format(Locale.ROOT,"%02d/%02d/%04d",dd,m+1,y);draw();},c.get(Calendar.YEAR),c.get(Calendar.MONTH),c.get(Calendar.DAY_OF_MONTH));d.getDatePicker().setMinDate(System.currentTimeMillis()-1000);d.show();});
  button(hour.isEmpty()?"Selecionar horário":hour,false).setOnClickListener(v->new TimePickerDialog(this,(p,h,m)->{hour=String.format(Locale.ROOT,"%02d:%02d",h,m);draw();},9,0,true).show());
  button("Continuar →",true).setOnClickListener(v->{if(day.isEmpty()||hour.isEmpty())toast("Informe data e horário.");else next();});
  button("← Voltar",false).setOnClickListener(v->previous());
 }
 void confirmPage(){
  title("Quase tudo pronto.","Revise o resumo e envie sua solicitação para a Roseli.");
  text("Seu nome",14,true,wine);ePerson=field("Nome completo",person,1);
  text("WhatsApp",14,true,wine);ePhone=field("DDD + número",phone,3);
  text("Observações",14,true,wine);eNotes=field("Peça, tecido, quantidade e cuidados",notes,1);
  text("RESUMO DO PEDIDO",12,true,0xffa27f43);
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