<?php

$category = $_POST["category"];
$password = $_POST["password"];
$title    = $_POST["title"];

 
$con = mysqli_connect("localhost","root","","bbs")or die("Mysql접속 실패!!!");


/////////인덱스 번호 매기기////////////

$sql ="SELECT * FROM noticeboard ORDER BY idx DESC limit 1";
$result = mysqli_query($con,$sql);
$row = mysqli_fetch_array($result);

if($row[0]==null)
{
	$idx = 1;
}
else
{
	$idx = $row[0]+1;
}

///////////////////////////////////////


//$category = "일본어";
//$title = "일본어 배우기";

$view = 0;
$date = date("Y-m-d H:i");

//$password = "1234";

$sql = "INSERT INTO noticeboard VALUES(".$idx.",'".$category."','".$title."',".$view.",'".$date."','".$password."')";

$ret = mysqli_query($con,$sql);

if($ret)
{
	echo "데이터가 성공적으로 입력됨";
}
else
{
	echo "데이터 입력 실패";
}



mysqli_close($con);

 
?>