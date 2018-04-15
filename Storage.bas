B4A=true
Group=Default Group
ModulesStructureVersion=1
Type=StaticCode
Version=7.8
@EndOfDesignText@
'Code module
'Subs in this code module will be accessible from all modules.
Sub Process_Globals
	'These global variables will be declared once when the application starts.
	'These variables can be accessed from all modules.
     
End Sub

Sub  LoadData  As List
	Dim dataArr As List
	dataArr.Initialize
	If File.Exists(File.DirInternal,"data") Then
		  Return File.ReadList(File.DirInternal,"data")
	End If
 
	Return dataArr
End Sub


Sub SaveData(Arr As List) 
	File.WriteList(File.DirInternal,"data",Arr)
End Sub

Sub AddData(Data As Map)
Dim dataArr  As List = LoadData
Dim json As JSONGenerator
json.Initialize(Data)
dataArr.Add(json.ToString)
SaveData(dataArr)
End Sub