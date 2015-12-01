var Pmin = 20;
var Dmax = 0;

var Pmax = 200;
var Dmax = 100;

var Pbest = Pmin;
var Dbest = Dmin;

var Piteracje = 10;
var Diteracje = Piteracje;

var predkosc = 2.0;
var czas = 3000; // ms
var ilosc_pomiarow = 30;

var bestkryterium = 10000000000;

function sleep(millis)
 {
  var date = new Date();
  var curDate = null;
  do { curDate = new Date(); }
  while(curDate-date < millis);
}

function kryterium(pomiary)
{
	return 0;
}

for(var P = Pmin; P < Pmax; P += (Pmax - Pmin)/Piteracje)
{
	for(var D = Dmin; D < Dmax; D += (Dmax - Dmin)/Diteracje)
	{
		RobotSetVel(predkosc,0,czas);
		var pomiary = [];
		var interwal = czas/ilosc_pomiarow;
		for(var i = 0; i < czas; i += interwal)
		{
			pomiary.push(RobotGetVel());
			sleep(interwal);
		}
		var obecneKryterium = kryterium(pomiary);
		if(obecneKryterium < bestkryterium)
		{
			bestkryterium = obecneKryterium;
			Pbest = P;
			Dbest = D;
		}
	}
}


