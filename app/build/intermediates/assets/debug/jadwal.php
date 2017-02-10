<html>  
    <head>  
    <meta charset="utf-8">  
    <meta name="viewport" content="width=device-width, initial-scale=1">  
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">    
  
    <link rel="stylesheet" href="Jquery/dily.css">  
    <script type="text/javascript" src="Jquery/jquery-1.6.4.min.js"></script>  
    <script type="text/javascript" src="Jquery/jquery-1.6.4.mobile.js"></script>  
</head>  
<style type="text/css">
   .left    { text-align: left;}
   .right   { text-align: right;}
   .center  { text-align: center;}
   .justify { text-align: justify;}
</style>
<body >   
</br>
<p class="justify"><b><center>JADWAL DOKTER HOSVITAL</center></b></p></br>
<?php
$host="febryituery.com";
$username="febryit1";
$pwd="febrydwiputra1234";
$db="febryit1_log";

$con=mysqli_connect($host,$username,$pwd,$db) or die ('Unable to connect database');

if(mysqli_connect_error($con)){
	echo "Failed to Connect database".mysqli_connect_error();
}
$i=0;
$query = mysqli_query($con,"Select * from dokter");
	while($row=mysqli_fetch_array($query)){
		$i++
	?>
<p class="justify"><center><?php echo $row['nama']?> - <?php echo $row['spesialis']?></center></p>
<p class="justify"><center><?php echo $row['hari']?></center></p>
<p class="justify"><center>Pukul 09.00 - 20.00</center></p></br>
<?php } ?>
</br>
<center><b>HOSVITAL 2016</b></center>
    <footer data-role="footer" data-theme="b">
    </footer>
</div>
</body>  
</html>