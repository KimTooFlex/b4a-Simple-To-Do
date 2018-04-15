B4A=true
Group=Default Group
ModulesStructureVersion=1
Type=Activity
Version=7.8
@EndOfDesignText@
#Region  Activity Attributes 
	#FullScreen: False
	#IncludeTitle: false
#End Region

Sub Process_Globals
	'These global variables will be declared once when the application starts.
	'These variables can be accessed from all modules.

End Sub

Sub Globals
	'These global variables will be redeclared each time the activity is created.
	'These variables can only be accessed from this module.

	Private txtName As EditText
	Private txtDesc As EditText
End Sub

Sub Activity_Create(FirstTime As Boolean)
	'Do not forget to load the layout file created with the visual designer. For example:
	 Activity.LoadLayout("frmManageTask")

End Sub

Sub Activity_Resume

End Sub

Sub Activity_Pause (UserClosed As Boolean)

End Sub


Sub btnBack_Click
	 Activity.Finish  'clossse activity
End Sub

Sub btnSave_Click
	'validate the data and save to file using map/dictonary
	
	If txtName.Text.Trim.Length==0 Then
		txtName.RequestFocus
		ToastMessageShow("Enter Name",False)
		Return
	Else If txtDesc.Text.Trim.Length==0 Then
		txtDesc.RequestFocus
		ToastMessageShow("Enter Name",False)
		Return
	End If
	
	Dim taskObject As Map
	
	taskObject.Initialize
	taskObject.Put("name",txtName.Text)
	taskObject.Put("desc",txtDesc.Text)
	taskObject.Put("done",False)
	
	Storage.AddData(taskObject)
	ToastMessageShow("Successfully Saved",False)
	Activity.Finish
	
End Sub