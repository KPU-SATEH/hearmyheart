<?php

$idx = (int)$_POST["idx"];
$order = $_POST["order"];
$word =  $_POST["word"];


$idx2 = (int)$_POST["idx2"];
$order2 = $_POST["order2"];
$word2 =  $_POST["word2"];


$idx3 = (int)$_POST["idx3"];
$order3 = $_POST["order3"];
$word3 =  $_POST["word3"];

$con = mysqli_connect("localhost","root","","bbs")or die("Mysql접속 실패!!!");


/////////인덱스 번호 매기기////////////
$sql ="SELECT * FROM word_3 ORDER BY idx DESC limit 1";
$result = mysqli_query($con,$sql);
$row = mysqli_fetch_array($result);
if($row[0]==null)
{
	$id = 1;
}
else
{
	$id = $row[0]+1;
}
///////////////////////////////////////


$sql = "INSERT INTO word_3 VALUES(".$id.",".$idx.",'".$order."','".$word."')";

$ret = mysqli_query($con,$sql);
if($ret)
{
	echo "데이터가 성공적으로 입력됨";
}
else
{
	echo "데이터 입력 실패";
}







/////////인덱스 번호 매기기////////////
$sql ="SELECT * FROM word_3 ORDER BY idx DESC limit 1";
$result = mysqli_query($con,$sql);
$row = mysqli_fetch_array($result);
if($row[0]==null)
{
	$id = 1;
}
else
{
	$id = $row[0]+1;
}
///////////////////////////////////////


$sql = "INSERT INTO word_3 VALUES(".$id.",".$idx2.",'".$order2."','".$word2."')";

$ret = mysqli_query($con,$sql);
if($ret)
{
	echo "데이터가 성공적으로 입력됨";
}
else
{
	echo "데이터 입력 실패";
}






/////////인덱스 번호 매기기////////////
$sql ="SELECT * FROM word_3 ORDER BY idx DESC limit 1";
$result = mysqli_query($con,$sql);
$row = mysqli_fetch_array($result);
if($row[0]==null)
{
	$id = 1;
}
else
{
	$id = $row[0]+1;
}
///////////////////////////////////////


$sql = "INSERT INTO word_3 VALUES(".$id.",".$idx3.",'".$order3."','".$word3."')";

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