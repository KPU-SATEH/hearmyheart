<?php  

$fcs = "의식주";
 
function unistr_to_xnstr($str){ 
    return preg_replace('/\\\u([a-z0-9]{4})/i', "&#x\\1;", $str); 
} 
 
$con=mysqli_connect("127.0.0.1","root","","bbs");  
  
if (mysqli_connect_errno($con))  
{  
   echo "Failed to connect to MySQL: " . mysqli_connect_error();  
}  
 
 
mysqli_set_charset($con,"utf8");  
 
 
$res = mysqli_query($con,"select * from noticeboard where category='".$fcs."'");  
   
$result = array();  
   
while($row = mysqli_fetch_array($res)){  
  array_push($result,  
    array('idx'=>$row[0],'category'=>$row[1],'title'=>$row[2] ,'view'=>$row[3],'date'=>$row[4],'password'=>$row[5],'bad'=>$row[6],'good'=>$row[7]   
    ));  
}  
   
 
$json = json_encode(array("result"=>$result));
echo $json;
 
   
mysqli_close($con);  
   
?>