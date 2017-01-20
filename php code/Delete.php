<?php

$idx = (int)$_POST["idx"];

 
$con = mysqli_connect("localhost","root","","bbs")or die("Mysql접속 실패!!!");


$ret = mysqli_query($con,"DELETE FROM noticeboard WHERE idx=".$idx."");

if($ret)
{
	echo "데이터가 성공적으로 삭제됨";
}
else
{
 	echo "데이터 삭제가 실패됨";
}

mysqli_close($con);

 
?>