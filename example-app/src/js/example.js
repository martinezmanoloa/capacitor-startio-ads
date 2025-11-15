import { StartioAds } from '@martinezmanoloa/capacitor-startio-ads';

window.testEcho = () => {
    const inputValue = document.getElementById("echoInput").value;
    StartioAds.echo({ value: inputValue })
}
