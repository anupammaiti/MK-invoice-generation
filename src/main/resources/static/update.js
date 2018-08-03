const postData = (url = ``, data = {}) => {
    // Default options are marked with *
    return fetch(url, {
        method: "POST", // *GET, POST, PUT, DELETE, etc.
        mode: "cors", // no-cors, cors, *same-origin
        cache: "no-cache", // *default, no-cache, reload, force-cache, only-if-cached
        credentials: "same-origin", // include, same-origin, *omit
        headers: {
            "Content-Type": "application/json; charset=utf-8",
            // "Content-Type": "application/x-www-form-urlencoded",
        },
        redirect: "follow", // manual, *follow, error
        referrer: "no-referrer", // no-referrer, *client
        body: JSON.stringify(data), // body data type must match "Content-Type" header
    })
        .then(response => response.text()) // parses response to JSON
        .catch(error => console.error(`Fetch Error =\n`, error));
};

const API_ID = '5f115a801bac495aae54adada565272f';

/**
 * POST REQUEST TO THE 'UPDATE' INVOICE CONTROLLER
 * The checkbox corresponding to an element should have the element's id as its own id, but with
 * the word box attached to it. e.g. element-> id="client" & checkbox-> id="clientBox"
 * Id of element is retrieved by removing the string 'Box' from the Id of current checkbox.
 * IF user checks box, they can edit value of corresponding element. If they uncheck the box,
 * element will be assigned its initial value.
 */
document.addEventListener('DOMContentLoaded', function () {
    const checkboxes = document.getElementsByName('editBox');
    const defaultFromCurrency = document.getElementById('fromCurrency').value;
    const defaultToCurrency = document.getElementById('toCurrency').value;
    const defaultExchangeRate = document.getElementById('exchangeRate').value;

    $.getJSON('https://openexchangerates.org/api/latest.json?app_id='+API_ID,
        function(data) {
            // Check money.js has finished loading:
            if ( typeof fx !== "undefined" && fx.rates ) {
                fx.rates = data.rates;
                fx.base = data.base;
            }
            else {
                var fxSetup = {
                    rates : data.rates,
                    base : data.base
                }
            }

            for(let i=0; i<checkboxes.length; i++){
                //This conditional is entered only if the checkbox will trigger a "set" of inputs
                if((checkboxes[i].id).endsWith('Group'))
                {
                    let divId = (checkboxes[i].id).replace('Group', 'Div');
                    let divElement = document.getElementById(divId);
                    let groupValues = divElement.children;
                    let group = [];
                    for(let k=0; k<groupValues.length; k++){
                        let obj = {id: (groupValues[k].firstChild).id, defaultValue: (groupValues[k].firstChild).value};
                        group.push(obj);
                    }
                    checkboxes[i].onclick = function () {
                        if(checkboxes[i].checked){
                            divElement.style.display = 'block';
                        }
                        else {
                            divElement.style.display = 'none';
                            group.forEach((obj) =>{
                                document.getElementById(obj.id).value = obj.defaultValue;
                            });
                        }
                    }
                }

                else if((checkboxes[i].id).endsWith('Text')){
                    let elementId = (checkboxes[i].id).slice(0,-4); //e.g. invoiceNumber
                    let divId = elementId+'Div'; //e.g. invoiceNumberDiv
                    let element = document.getElementById(elementId);
                    let defaultValue = element.value;

                    checkboxes[i].onclick = function () {
                        if(checkboxes[i].checked){
                            element.value = '';
                            document.getElementById(divId).style.display = 'block';
                        }
                        else {
                            document.getElementById(divId).style.display = 'none';
                            element.value = defaultValue;
                        }
                    }
                }
                //This conditional will be entered if the checkbox will trigger 1 input, manual or otherwise
                //Checkbox id is the same as element Id, but has the string 'Box' to the end. e.g. 'portfolioBox'
                else {
                    let elementId = (checkboxes[i].id).slice(0,-3); //e.g. invoiceNumber
                    let divId = elementId+'Div'; //e.g. invoiceNumberDiv
                    let manualId = elementId+'Manual';//e.g. invoiceNumberManual
                    let element = document.getElementById(elementId);
                    let defaultValue = element.value;

                    checkboxes[i].onclick = function () {
                        if(checkboxes[i].checked)
                            document.getElementById(divId).style.display = 'block';
                        else {
                            document.getElementById(divId).style.display = 'none';
                            element.value = defaultValue;
                            if(document.getElementById(manualId)!== null)
                                document.getElementById(manualId).value = '';
                        }
                    }
                }
            }

            //Script will set value of variable as null, if it is equal to an empty string
            document.getElementById('updateButton').addEventListener('click', function () {
                let myData = {};
                const newValues = document.getElementsByName('newValue');
                const groupValues = document.getElementsByName('groupValue');
                const manualValues = document.getElementsByName('manualValue');

                function setValuesToObj(arrayElement){
                    let elementId = arrayElement.id;
                    myData[elementId] = document.getElementById(elementId).value;
                }

                //Function will detect manual values, and if they are set, it will set the
                //non-manual values to null.
                function setManualValuesToObj(arrayElement){
                    const keyword = 'Manual';
                    let manualElementId = arrayElement.id;
                    let elementId = manualElementId.slice(0,-keyword.length);
                    let manualInput = document.getElementById(manualElementId).value;
                    let input = document.getElementById(elementId).value;

                    if(manualInput===''){
                        myData[elementId] = input;
                        myData[manualElementId] = null;
                    }
                    else{
                        myData[manualElementId] = manualInput;
                        myData[elementId] = null;
                    }
                }

                newValues.forEach(setValuesToObj);
                groupValues.forEach(setValuesToObj);
                manualValues.forEach(setManualValuesToObj);
                myData.invoiceId = parseInt(document.getElementById('invoiceId').value);
                myData.companyId = parseInt(document.getElementById('companyId').value);

                if(defaultFromCurrency!==document.getElementById('fromCurrency').value ||
                defaultToCurrency!==document.getElementById('toCurrency').value &&
                document.getElementById('exchangeRate').value === defaultExchangeRate){
                  let base = $('#fromCurrency option:selected').text();
                  let target = $('#toCurrency option:selected').text();

                  myData.exchangeRate = fx.convert(1, {from: base, to: target});
                 }
                 console.log(myData);

                postData('/find/update/execute', myData).then(() =>
                {
                    window.location = '/success/updated';
                });
            });
    });
});