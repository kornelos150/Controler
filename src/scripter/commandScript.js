var BluethoothClass = Java.type('bluethoothCtrl.BluetoothImp');
//var BluethoothClass = Java.type('bluethoothCtrl.BluetoothMock');
var btObject = BluethoothClass.getInstance();

function decode(text)
{
	var res = (text.replaceAll(',','.')).split("!");
	var arr = [];
	for(var i = 0; i < res.length; ++i)
	{
		arr.push(parseFloat(res[i]));
	}
	return arr;
}

function decodeInt(text)
{
	var res = text.split("!");
	var arr = [];
	for(var i = 0; i < res.length; ++i)
	{
		arr.push(parseInt(res[i]));
	}
	return arr;
}

function RobotSetVel(translate,rotate,time)
{
	var result = btObject.setVelocity(translate,rotate,time);
	return decode(result);
}

function RobotGetVel()
{
	var result = btObject.getVelocity();
	return decode(result);
}

function RobotSetConf(pVal,dVal)
{
	var result = btObject.setConfig(pVal,dVal);
	return decode(result);
}

function RobotGetConf()
{
	var result = btObject.getConfig();
	return decode(result);
}

function RobotSetTimeout(timeout)
{
	var result = btObject.setTimeout(timeout);
	return parseInt(result);
}

function RobotGetTimeout()
{
	var result = btObject.getTimeout();
	return parseInt(result);
}

function RobotSetPWM(left,right,time)
{
	var result = btObject.setPWM(left,right,time);
	return decodeInt(result);
}

function RobotGetPWM()
{
	var result = btObject.getPWM();
	return decodeInt(result);
}

function RobotSetEncoder(time)
{
	var result = btObject.setEncoderMeas(time);
	return parseInt(result);
}

function RobotGetEncoder()
{
	var result = btObject.getEncoderMeas();
	return parseInt(result);
}
function RobotSetRegTim(time)
{
	var result = btObject.setRegulationTimer(time);
	return parseInt(result);
}

function RobotGetRegTim()
{
	var result = btObject.getRegulationTimer();
	return parseInt(result);
}

function RobotSetDirection(bitmap)
{
	var result = btObject.setMotorDirection(bitmap);
	return parseInt(result);
}
function RobotGetDirection()
{
	var result = btObject.getMotorDirection();
	return parseInt(result);
}

function RobotGetSetSpeed()
{
	var result = btObject.getSetSpeed();
	return decode(result);
}
