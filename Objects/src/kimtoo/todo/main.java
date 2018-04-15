package kimtoo.todo;


import anywheresoftware.b4a.B4AMenuItem;
import android.app.Activity;
import android.os.Bundle;
import anywheresoftware.b4a.BA;
import anywheresoftware.b4a.BALayout;
import anywheresoftware.b4a.B4AActivity;
import anywheresoftware.b4a.ObjectWrapper;
import anywheresoftware.b4a.objects.ActivityWrapper;
import java.lang.reflect.InvocationTargetException;
import anywheresoftware.b4a.B4AUncaughtException;
import anywheresoftware.b4a.debug.*;
import java.lang.ref.WeakReference;

public class main extends Activity implements B4AActivity{
	public static main mostCurrent;
	static boolean afterFirstLayout;
	static boolean isFirst = true;
    private static boolean processGlobalsRun = false;
	BALayout layout;
	public static BA processBA;
	BA activityBA;
    ActivityWrapper _activity;
    java.util.ArrayList<B4AMenuItem> menuItems;
	public static final boolean fullScreen = false;
	public static final boolean includeTitle = false;
    public static WeakReference<Activity> previousOne;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (processBA == null) {
			processBA = new BA(this.getApplicationContext(), null, null, "kimtoo.todo", "kimtoo.todo.main");
			processBA.loadHtSubs(this.getClass());
	        float deviceScale = getApplicationContext().getResources().getDisplayMetrics().density;
	        BALayout.setDeviceScale(deviceScale);
            
		}
		else if (previousOne != null) {
			Activity p = previousOne.get();
			if (p != null && p != this) {
                BA.LogInfo("Killing previous instance (main).");
				p.finish();
			}
		}
        processBA.runHook("oncreate", this, null);
		if (!includeTitle) {
        	this.getWindow().requestFeature(android.view.Window.FEATURE_NO_TITLE);
        }
        if (fullScreen) {
        	getWindow().setFlags(android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN,   
        			android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
		mostCurrent = this;
        processBA.sharedProcessBA.activityBA = null;
		layout = new BALayout(this);
		setContentView(layout);
		afterFirstLayout = false;
        WaitForLayout wl = new WaitForLayout();
        if (anywheresoftware.b4a.objects.ServiceHelper.StarterHelper.startFromActivity(processBA, wl, false))
		    BA.handler.postDelayed(wl, 5);

	}
	static class WaitForLayout implements Runnable {
		public void run() {
			if (afterFirstLayout)
				return;
			if (mostCurrent == null)
				return;
            
			if (mostCurrent.layout.getWidth() == 0) {
				BA.handler.postDelayed(this, 5);
				return;
			}
			mostCurrent.layout.getLayoutParams().height = mostCurrent.layout.getHeight();
			mostCurrent.layout.getLayoutParams().width = mostCurrent.layout.getWidth();
			afterFirstLayout = true;
			mostCurrent.afterFirstLayout();
		}
	}
	private void afterFirstLayout() {
        if (this != mostCurrent)
			return;
		activityBA = new BA(this, layout, processBA, "kimtoo.todo", "kimtoo.todo.main");
        
        processBA.sharedProcessBA.activityBA = new java.lang.ref.WeakReference<BA>(activityBA);
        anywheresoftware.b4a.objects.ViewWrapper.lastId = 0;
        _activity = new ActivityWrapper(activityBA, "activity");
        anywheresoftware.b4a.Msgbox.isDismissing = false;
        if (BA.isShellModeRuntimeCheck(processBA)) {
			if (isFirst)
				processBA.raiseEvent2(null, true, "SHELL", false);
			processBA.raiseEvent2(null, true, "CREATE", true, "kimtoo.todo.main", processBA, activityBA, _activity, anywheresoftware.b4a.keywords.Common.Density, mostCurrent);
			_activity.reinitializeForShell(activityBA, "activity");
		}
        initializeProcessGlobals();		
        initializeGlobals();
        
        BA.LogInfo("** Activity (main) Create, isFirst = " + isFirst + " **");
        processBA.raiseEvent2(null, true, "activity_create", false, isFirst);
		isFirst = false;
		if (this != mostCurrent)
			return;
        processBA.setActivityPaused(false);
        BA.LogInfo("** Activity (main) Resume **");
        processBA.raiseEvent(null, "activity_resume");
        if (android.os.Build.VERSION.SDK_INT >= 11) {
			try {
				android.app.Activity.class.getMethod("invalidateOptionsMenu").invoke(this,(Object[]) null);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}
	public void addMenuItem(B4AMenuItem item) {
		if (menuItems == null)
			menuItems = new java.util.ArrayList<B4AMenuItem>();
		menuItems.add(item);
	}
	@Override
	public boolean onCreateOptionsMenu(android.view.Menu menu) {
		super.onCreateOptionsMenu(menu);
        try {
            if (processBA.subExists("activity_actionbarhomeclick")) {
                Class.forName("android.app.ActionBar").getMethod("setHomeButtonEnabled", boolean.class).invoke(
                    getClass().getMethod("getActionBar").invoke(this), true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (processBA.runHook("oncreateoptionsmenu", this, new Object[] {menu}))
            return true;
		if (menuItems == null)
			return false;
		for (B4AMenuItem bmi : menuItems) {
			android.view.MenuItem mi = menu.add(bmi.title);
			if (bmi.drawable != null)
				mi.setIcon(bmi.drawable);
            if (android.os.Build.VERSION.SDK_INT >= 11) {
				try {
                    if (bmi.addToBar) {
				        android.view.MenuItem.class.getMethod("setShowAsAction", int.class).invoke(mi, 1);
                    }
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			mi.setOnMenuItemClickListener(new B4AMenuItemsClickListener(bmi.eventName.toLowerCase(BA.cul)));
		}
        
		return true;
	}   
 @Override
 public boolean onOptionsItemSelected(android.view.MenuItem item) {
    if (item.getItemId() == 16908332) {
        processBA.raiseEvent(null, "activity_actionbarhomeclick");
        return true;
    }
    else
        return super.onOptionsItemSelected(item); 
}
@Override
 public boolean onPrepareOptionsMenu(android.view.Menu menu) {
    super.onPrepareOptionsMenu(menu);
    processBA.runHook("onprepareoptionsmenu", this, new Object[] {menu});
    return true;
    
 }
 protected void onStart() {
    super.onStart();
    processBA.runHook("onstart", this, null);
}
 protected void onStop() {
    super.onStop();
    processBA.runHook("onstop", this, null);
}
    public void onWindowFocusChanged(boolean hasFocus) {
       super.onWindowFocusChanged(hasFocus);
       if (processBA.subExists("activity_windowfocuschanged"))
           processBA.raiseEvent2(null, true, "activity_windowfocuschanged", false, hasFocus);
    }
	private class B4AMenuItemsClickListener implements android.view.MenuItem.OnMenuItemClickListener {
		private final String eventName;
		public B4AMenuItemsClickListener(String eventName) {
			this.eventName = eventName;
		}
		public boolean onMenuItemClick(android.view.MenuItem item) {
			processBA.raiseEventFromUI(item.getTitle(), eventName + "_click");
			return true;
		}
	}
    public static Class<?> getObject() {
		return main.class;
	}
    private Boolean onKeySubExist = null;
    private Boolean onKeyUpSubExist = null;
	@Override
	public boolean onKeyDown(int keyCode, android.view.KeyEvent event) {
        if (processBA.runHook("onkeydown", this, new Object[] {keyCode, event}))
            return true;
		if (onKeySubExist == null)
			onKeySubExist = processBA.subExists("activity_keypress");
		if (onKeySubExist) {
			if (keyCode == anywheresoftware.b4a.keywords.constants.KeyCodes.KEYCODE_BACK &&
					android.os.Build.VERSION.SDK_INT >= 18) {
				HandleKeyDelayed hk = new HandleKeyDelayed();
				hk.kc = keyCode;
				BA.handler.post(hk);
				return true;
			}
			else {
				boolean res = new HandleKeyDelayed().runDirectly(keyCode);
				if (res)
					return true;
			}
		}
		return super.onKeyDown(keyCode, event);
	}
	private class HandleKeyDelayed implements Runnable {
		int kc;
		public void run() {
			runDirectly(kc);
		}
		public boolean runDirectly(int keyCode) {
			Boolean res =  (Boolean)processBA.raiseEvent2(_activity, false, "activity_keypress", false, keyCode);
			if (res == null || res == true) {
                return true;
            }
            else if (keyCode == anywheresoftware.b4a.keywords.constants.KeyCodes.KEYCODE_BACK) {
				finish();
				return true;
			}
            return false;
		}
		
	}
    @Override
	public boolean onKeyUp(int keyCode, android.view.KeyEvent event) {
        if (processBA.runHook("onkeyup", this, new Object[] {keyCode, event}))
            return true;
		if (onKeyUpSubExist == null)
			onKeyUpSubExist = processBA.subExists("activity_keyup");
		if (onKeyUpSubExist) {
			Boolean res =  (Boolean)processBA.raiseEvent2(_activity, false, "activity_keyup", false, keyCode);
			if (res == null || res == true)
				return true;
		}
		return super.onKeyUp(keyCode, event);
	}
	@Override
	public void onNewIntent(android.content.Intent intent) {
        super.onNewIntent(intent);
		this.setIntent(intent);
        processBA.runHook("onnewintent", this, new Object[] {intent});
	}
    @Override 
	public void onPause() {
		super.onPause();
        if (_activity == null) //workaround for emulator bug (Issue 2423)
            return;
		anywheresoftware.b4a.Msgbox.dismiss(true);
        BA.LogInfo("** Activity (main) Pause, UserClosed = " + activityBA.activity.isFinishing() + " **");
        if (mostCurrent != null)
            processBA.raiseEvent2(_activity, true, "activity_pause", false, activityBA.activity.isFinishing());		
        processBA.setActivityPaused(true);
        mostCurrent = null;
        if (!activityBA.activity.isFinishing())
			previousOne = new WeakReference<Activity>(this);
        anywheresoftware.b4a.Msgbox.isDismissing = false;
        processBA.runHook("onpause", this, null);
	}

	@Override
	public void onDestroy() {
        super.onDestroy();
		previousOne = null;
        processBA.runHook("ondestroy", this, null);
	}
    @Override 
	public void onResume() {
		super.onResume();
        mostCurrent = this;
        anywheresoftware.b4a.Msgbox.isDismissing = false;
        if (activityBA != null) { //will be null during activity create (which waits for AfterLayout).
        	ResumeMessage rm = new ResumeMessage(mostCurrent);
        	BA.handler.post(rm);
        }
        processBA.runHook("onresume", this, null);
	}
    private static class ResumeMessage implements Runnable {
    	private final WeakReference<Activity> activity;
    	public ResumeMessage(Activity activity) {
    		this.activity = new WeakReference<Activity>(activity);
    	}
		public void run() {
			if (mostCurrent == null || mostCurrent != activity.get())
				return;
			processBA.setActivityPaused(false);
            BA.LogInfo("** Activity (main) Resume **");
		    processBA.raiseEvent(mostCurrent._activity, "activity_resume", (Object[])null);
		}
    }
	@Override
	protected void onActivityResult(int requestCode, int resultCode,
	      android.content.Intent data) {
		processBA.onActivityResult(requestCode, resultCode, data);
        processBA.runHook("onactivityresult", this, new Object[] {requestCode, resultCode});
	}
	private static void initializeGlobals() {
		processBA.raiseEvent2(null, true, "globals", false, (Object[])null);
	}
    public void onRequestPermissionsResult(int requestCode,
        String permissions[], int[] grantResults) {
        for (int i = 0;i < permissions.length;i++) {
            Object[] o = new Object[] {permissions[i], grantResults[i] == 0};
            processBA.raiseEventFromDifferentThread(null,null, 0, "activity_permissionresult", true, o);
        }
            
    }

public anywheresoftware.b4a.keywords.Common __c = null;
public anywheresoftware.b4a.objects.ListViewWrapper _listview1 = null;
public kimtoo.todo.starter _starter = null;
public kimtoo.todo.managetask _managetask = null;
public kimtoo.todo.storage _storage = null;

public static boolean isAnyActivityVisible() {
    boolean vis = false;
vis = vis | (main.mostCurrent != null);
vis = vis | (managetask.mostCurrent != null);
return vis;}
public static String  _activity_create(boolean _firsttime) throws Exception{
 //BA.debugLineNum = 29;BA.debugLine="Sub Activity_Create(FirstTime As Boolean)";
 //BA.debugLineNum = 31;BA.debugLine="Activity.LoadLayout(\"frmHome\")";
mostCurrent._activity.LoadLayout("frmHome",mostCurrent.activityBA);
 //BA.debugLineNum = 32;BA.debugLine="ListView1.TwoLinesAndBitmap.Label.TextColor=Color";
mostCurrent._listview1.getTwoLinesAndBitmap().Label.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.DarkGray);
 //BA.debugLineNum = 34;BA.debugLine="LoadData";
_loaddata();
 //BA.debugLineNum = 37;BA.debugLine="End Sub";
return "";
}
public static String  _activity_pause(boolean _userclosed) throws Exception{
 //BA.debugLineNum = 43;BA.debugLine="Sub Activity_Pause (UserClosed As Boolean)";
 //BA.debugLineNum = 45;BA.debugLine="End Sub";
return "";
}
public static String  _activity_resume() throws Exception{
 //BA.debugLineNum = 39;BA.debugLine="Sub Activity_Resume";
 //BA.debugLineNum = 40;BA.debugLine="LoadData";
_loaddata();
 //BA.debugLineNum = 41;BA.debugLine="End Sub";
return "";
}
public static String  _addtodolist(String _text,String _description,boolean _done,anywheresoftware.b4a.objects.collections.Map _item) throws Exception{
String _icon = "";
 //BA.debugLineNum = 105;BA.debugLine="Sub addTodoList(Text As String,Description As Stri";
 //BA.debugLineNum = 106;BA.debugLine="Dim icon As String =\"wait.png\"";
_icon = "wait.png";
 //BA.debugLineNum = 107;BA.debugLine="If done Then";
if (_done) { 
 //BA.debugLineNum = 108;BA.debugLine="icon=\"complete.png\"";
_icon = "complete.png";
 };
 //BA.debugLineNum = 110;BA.debugLine="ListView1.AddTwoLinesAndBitmap2(Text,Description,";
mostCurrent._listview1.AddTwoLinesAndBitmap2(BA.ObjectToCharSequence(_text),BA.ObjectToCharSequence(_description),(android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),_icon).getObject()),(Object)(_item.getObject()));
 //BA.debugLineNum = 112;BA.debugLine="End Sub";
return "";
}
public static String  _btnadd_click() throws Exception{
 //BA.debugLineNum = 83;BA.debugLine="Sub btnAdd_Click";
 //BA.debugLineNum = 84;BA.debugLine="StartActivity(ManageTask)";
anywheresoftware.b4a.keywords.Common.StartActivity(processBA,(Object)(mostCurrent._managetask.getObject()));
 //BA.debugLineNum = 85;BA.debugLine="End Sub";
return "";
}
public static String  _btnmenu_click() throws Exception{
 //BA.debugLineNum = 48;BA.debugLine="Sub btnMenu_Click";
 //BA.debugLineNum = 50;BA.debugLine="End Sub";
return "";
}
public static String  _globals() throws Exception{
 //BA.debugLineNum = 21;BA.debugLine="Sub Globals";
 //BA.debugLineNum = 26;BA.debugLine="Private ListView1 As ListView";
mostCurrent._listview1 = new anywheresoftware.b4a.objects.ListViewWrapper();
 //BA.debugLineNum = 27;BA.debugLine="End Sub";
return "";
}
public static String  _listview1_itemclick(int _position,Object _value) throws Exception{
 //BA.debugLineNum = 52;BA.debugLine="Sub ListView1_ItemClick (Position As Int, Value As";
 //BA.debugLineNum = 54;BA.debugLine="End Sub";
return "";
}
public static String  _listview1_itemlongclick(int _position,Object _value) throws Exception{
anywheresoftware.b4a.objects.collections.List _opt = null;
anywheresoftware.b4a.objects.collections.Map _task = null;
int _r = 0;
anywheresoftware.b4a.objects.collections.JSONParser.JSONGenerator _tojson = null;
anywheresoftware.b4a.objects.collections.List _savedtasks = null;
 //BA.debugLineNum = 56;BA.debugLine="Sub ListView1_ItemLongClick (Position As Int, Valu";
 //BA.debugLineNum = 57;BA.debugLine="Dim opt As List";
_opt = new anywheresoftware.b4a.objects.collections.List();
 //BA.debugLineNum = 58;BA.debugLine="opt.Initialize2(Array As String(\"Toggle\",\"Delet";
_opt.Initialize2(anywheresoftware.b4a.keywords.Common.ArrayToList(new String[]{"Toggle","Delete"}));
 //BA.debugLineNum = 59;BA.debugLine="Dim Task As Map = Value";
_task = new anywheresoftware.b4a.objects.collections.Map();
_task.setObject((anywheresoftware.b4a.objects.collections.Map.MyMap)(_value));
 //BA.debugLineNum = 60;BA.debugLine="Dim r As Int =  InputList(opt,\"\",-1)";
_r = anywheresoftware.b4a.keywords.Common.InputList(_opt,BA.ObjectToCharSequence(""),(int) (-1),mostCurrent.activityBA);
 //BA.debugLineNum = 62;BA.debugLine="If r==0 Then";
if (_r==0) { 
 //BA.debugLineNum = 64;BA.debugLine="Task.Put(\"done\", Not( Task.Get(\"done\")))";
_task.Put((Object)("done"),(Object)(anywheresoftware.b4a.keywords.Common.Not(BA.ObjectToBoolean(_task.Get((Object)("done"))))));
 //BA.debugLineNum = 65;BA.debugLine="Dim toJson As JSONGenerator";
_tojson = new anywheresoftware.b4a.objects.collections.JSONParser.JSONGenerator();
 //BA.debugLineNum = 66;BA.debugLine="toJson.Initialize(Task)";
_tojson.Initialize(_task);
 //BA.debugLineNum = 67;BA.debugLine="Dim SavedTasks As List= Storage.LoadData";
_savedtasks = new anywheresoftware.b4a.objects.collections.List();
_savedtasks = mostCurrent._storage._loaddata(mostCurrent.activityBA);
 //BA.debugLineNum = 68;BA.debugLine="SavedTasks.Set(Position,toJson.ToString)";
_savedtasks.Set(_position,(Object)(_tojson.ToString()));
 //BA.debugLineNum = 69;BA.debugLine="Storage.SaveData(SavedTasks)";
mostCurrent._storage._savedata(mostCurrent.activityBA,_savedtasks);
 //BA.debugLineNum = 70;BA.debugLine="LoadData";
_loaddata();
 }else if(_r==1) { 
 //BA.debugLineNum = 73;BA.debugLine="ToastMessageShow(\"Deleteing...\",False)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Deleteing..."),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 74;BA.debugLine="Dim SavedTasks As List= Storage.LoadData";
_savedtasks = new anywheresoftware.b4a.objects.collections.List();
_savedtasks = mostCurrent._storage._loaddata(mostCurrent.activityBA);
 //BA.debugLineNum = 75;BA.debugLine="SavedTasks.RemoveAt(Position)";
_savedtasks.RemoveAt(_position);
 //BA.debugLineNum = 76;BA.debugLine="Storage.SaveData(SavedTasks)";
mostCurrent._storage._savedata(mostCurrent.activityBA,_savedtasks);
 //BA.debugLineNum = 77;BA.debugLine="LoadData";
_loaddata();
 };
 //BA.debugLineNum = 81;BA.debugLine="End Sub";
return "";
}
public static String  _loaddata() throws Exception{
anywheresoftware.b4a.objects.collections.List _tastks = null;
String _json = "";
anywheresoftware.b4a.objects.collections.JSONParser _jsoncon = null;
anywheresoftware.b4a.objects.collections.Map _t = null;
 //BA.debugLineNum = 88;BA.debugLine="Sub LoadData";
 //BA.debugLineNum = 89;BA.debugLine="ListView1.Clear";
mostCurrent._listview1.Clear();
 //BA.debugLineNum = 91;BA.debugLine="Dim tastks As List =Storage.LoadData";
_tastks = new anywheresoftware.b4a.objects.collections.List();
_tastks = mostCurrent._storage._loaddata(mostCurrent.activityBA);
 //BA.debugLineNum = 93;BA.debugLine="For Each Json As String In tastks";
{
final anywheresoftware.b4a.BA.IterableList group3 = _tastks;
final int groupLen3 = group3.getSize()
;int index3 = 0;
;
for (; index3 < groupLen3;index3++){
_json = BA.ObjectToString(group3.Get(index3));
 //BA.debugLineNum = 94;BA.debugLine="Dim jsonCon As JSONParser";
_jsoncon = new anywheresoftware.b4a.objects.collections.JSONParser();
 //BA.debugLineNum = 95;BA.debugLine="jsonCon.Initialize(Json)";
_jsoncon.Initialize(_json);
 //BA.debugLineNum = 96;BA.debugLine="Dim T As Map = jsonCon.NextObject";
_t = new anywheresoftware.b4a.objects.collections.Map();
_t = _jsoncon.NextObject();
 //BA.debugLineNum = 97;BA.debugLine="addTodoList(T.Get(\"name\"),T.Get(\"desc\"),T.Get(";
_addtodolist(BA.ObjectToString(_t.Get((Object)("name"))),BA.ObjectToString(_t.Get((Object)("desc"))),BA.ObjectToBoolean(_t.Get((Object)("done"))),_t);
 }
};
 //BA.debugLineNum = 102;BA.debugLine="End Sub";
return "";
}

public static void initializeProcessGlobals() {
    
    if (main.processGlobalsRun == false) {
	    main.processGlobalsRun = true;
		try {
		        main._process_globals();
starter._process_globals();
managetask._process_globals();
storage._process_globals();
		
        } catch (Exception e) {
			throw new RuntimeException(e);
		}
    }
}public static String  _process_globals() throws Exception{
 //BA.debugLineNum = 15;BA.debugLine="Sub Process_Globals";
 //BA.debugLineNum = 19;BA.debugLine="End Sub";
return "";
}
}
