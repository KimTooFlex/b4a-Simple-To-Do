package kimtoo.todo;


import anywheresoftware.b4a.BA;
import anywheresoftware.b4a.BALayout;
import anywheresoftware.b4a.debug.*;

public class storage {
private static storage mostCurrent = new storage();
public static Object getObject() {
    throw new RuntimeException("Code module does not support this method.");
}
 public anywheresoftware.b4a.keywords.Common __c = null;
public kimtoo.todo.main _main = null;
public kimtoo.todo.starter _starter = null;
public kimtoo.todo.managetask _managetask = null;
public static String  _adddata(anywheresoftware.b4a.BA _ba,anywheresoftware.b4a.objects.collections.Map _data) throws Exception{
anywheresoftware.b4a.objects.collections.List _dataarr = null;
anywheresoftware.b4a.objects.collections.JSONParser.JSONGenerator _json = null;
 //BA.debugLineNum = 24;BA.debugLine="Sub AddData(Data As Map)";
 //BA.debugLineNum = 25;BA.debugLine="Dim dataArr  As List = LoadData";
_dataarr = new anywheresoftware.b4a.objects.collections.List();
_dataarr = _loaddata(_ba);
 //BA.debugLineNum = 26;BA.debugLine="Dim json As JSONGenerator";
_json = new anywheresoftware.b4a.objects.collections.JSONParser.JSONGenerator();
 //BA.debugLineNum = 27;BA.debugLine="json.Initialize(Data)";
_json.Initialize(_data);
 //BA.debugLineNum = 28;BA.debugLine="dataArr.Add(json.ToString)";
_dataarr.Add((Object)(_json.ToString()));
 //BA.debugLineNum = 29;BA.debugLine="SaveData(dataArr)";
_savedata(_ba,_dataarr);
 //BA.debugLineNum = 30;BA.debugLine="End Sub";
return "";
}
public static anywheresoftware.b4a.objects.collections.List  _loaddata(anywheresoftware.b4a.BA _ba) throws Exception{
anywheresoftware.b4a.objects.collections.List _dataarr = null;
 //BA.debugLineNum = 9;BA.debugLine="Sub  LoadData  As List";
 //BA.debugLineNum = 10;BA.debugLine="Dim dataArr As List";
_dataarr = new anywheresoftware.b4a.objects.collections.List();
 //BA.debugLineNum = 11;BA.debugLine="dataArr.Initialize";
_dataarr.Initialize();
 //BA.debugLineNum = 12;BA.debugLine="If File.Exists(File.DirInternal,\"data\") Then";
if (anywheresoftware.b4a.keywords.Common.File.Exists(anywheresoftware.b4a.keywords.Common.File.getDirInternal(),"data")) { 
 //BA.debugLineNum = 13;BA.debugLine="Return File.ReadList(File.DirInternal,\"data\")";
if (true) return anywheresoftware.b4a.keywords.Common.File.ReadList(anywheresoftware.b4a.keywords.Common.File.getDirInternal(),"data");
 };
 //BA.debugLineNum = 16;BA.debugLine="Return dataArr";
if (true) return _dataarr;
 //BA.debugLineNum = 17;BA.debugLine="End Sub";
return null;
}
public static String  _process_globals() throws Exception{
 //BA.debugLineNum = 3;BA.debugLine="Sub Process_Globals";
 //BA.debugLineNum = 7;BA.debugLine="End Sub";
return "";
}
public static String  _savedata(anywheresoftware.b4a.BA _ba,anywheresoftware.b4a.objects.collections.List _arr) throws Exception{
 //BA.debugLineNum = 20;BA.debugLine="Sub SaveData(Arr As List)";
 //BA.debugLineNum = 21;BA.debugLine="File.WriteList(File.DirInternal,\"data\",Arr)";
anywheresoftware.b4a.keywords.Common.File.WriteList(anywheresoftware.b4a.keywords.Common.File.getDirInternal(),"data",_arr);
 //BA.debugLineNum = 22;BA.debugLine="End Sub";
return "";
}
}
