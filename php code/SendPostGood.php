<?php
 
 $good = (int)$_POST["good"];
 $idx = (int)$_POST["idx"];
 
 $con = mysqli_connect("localhost","root","","bbs")or die("Mysql접속 실패!!");

 $sql = "UPDATE noticeboard SET good= '".$good."' WHERE idx=".$idx."";

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