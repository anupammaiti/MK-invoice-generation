const checkboxKeyword = 'Box';
const manualInputKeyword = 'Manual';
const textInputKeyword = 'Text';
const groupedValuesKeyword = 'Group';
const divKeyword = 'Div';

/**
 * Will send an object passed as parameter, as a POST http request to the specified url.
 * @param url The url to send an http request to.
 * @param data The object to send with the request.
 * @return {Promise<string | never | void>}
 */
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

/**
 *
 * @param array
 * @param data
 * @param doc
 */
function setValuesToObj(array = [], data={}, doc){
    array.forEach(function(arrayElement){
        let elementId = arrayElement.id;
        data[elementId] = doc.getElementById(elementId).value;
    });

    return data;
}

/**
 * Function will fetch elements that contain a 'keyword' in their id
 * and add them to an object passed as parameter. Elements with this keyword are
 * elements elements that accept both text input from the user and a selection from a drop down list.
 * If a text input is set, the function will use that value to populate the object, otherwise
 * If it is not set, the value from the dropdown list will be used.
 * @param array An array of elements to get their value.
 * @param data An object to be populated with the fetched elements' values.
 * @param doc The HTML document that contains the elements.
 * @param keyword
 * @return data An object containing the values of all elements whose id contain the keyword parameter
 */
function setManualValuesToObj(array=[], data={}, doc, keyword){
    array.forEach(function(arrayElement){
        let manualElementId = arrayElement.id;
        let elementId = manualElementId.slice(0,-(keyword.length));
        let manualInput = doc.getElementById(manualElementId).value;
        let input = doc.getElementById(elementId).value;

        data[elementId] = manualInput==='' ? data[elementId] = input : data[elementId] = manualInput;
    });

    return data;
}

function setCheckboxFunctionality(checkboxes=[]){
    for(let i=0; i<checkboxes.length; i++){
        //This conditional is entered only if the checkbox will trigger a "set" of inputs
        if((checkboxes[i].id).endsWith(groupedValuesKeyword))
        {
            let divId = (checkboxes[i].id).replace(groupedValuesKeyword, divKeyword);
            let divElement = document.getElementById(divId);
            let groupValues = divElement.children;
            let groupElements = [];
            for(let k=0; k<groupValues.length; k++){
                let obj = {
                    id: (groupValues[k].firstChild).id,
                    defaultValue: (groupValues[k].firstChild).value
                };
                groupElements.push(obj);
            }
            checkboxes[i].onclick = function () {
                if(checkboxes[i].checked){
                    divElement.style.display = 'block';
                }
                else {
                    divElement.style.display = 'none';
                    groupElements.forEach((obj) =>{
                        document.getElementById(obj.id).value = obj.defaultValue;
                    });
                }
            }
        }

        else if((checkboxes[i].id).endsWith(textInputKeyword)){
            let elementId = (checkboxes[i].id).slice(0,-(textInputKeyword.length)); //e.g. invoiceNumber
            let divId = elementId+divKeyword; //e.g. invoiceNumberDiv
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
            let elementId = (checkboxes[i].id).slice(0,-(checkboxKeyword.length)); //e.g. invoiceNumber
            let divId = elementId+divKeyword; //e.g. invoiceNumberDiv
            let manualId = elementId+manualInputKeyword;//e.g. invoiceNumberManual
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
}

/**
 * The checkbox corresponding to an element should have the element's id as its own id, but with
 * the word box attached to it. e.g. element-> id="client" & checkbox-> id="clientBox"
 * Id of element is retrieved by removing the string 'Box' from the Id of current checkbox.
 * IF user checks box, they can edit value of corresponding element. If they uncheck the box,
 * element will be assigned its initial value.
 *
 * @see <a href="https://github.com/soutzis/MK-invoice-generation/blob/cbf6b3f12d86fa885ba5b8dd972fe0f49c0c1a1d/src/main/resources/static/update.js">
 *     repository/update.js</a> for the version of update.js, where it used openexchangerate to automatically update rates.
 */
document.addEventListener('DOMContentLoaded', function () {
    const checkboxes = document.getElementsByName('editBox');
    const defaultFromCurrency = document.getElementById('fromCurrency').value;
    const defaultToCurrency = document.getElementById('toCurrency').value;
    const defaultExchangeRate = document.getElementById('exchangeRate').value;
    const companyId = document.getElementById('companyId').value;

    setCheckboxFunctionality(checkboxes);

    //Script will set value of variable as null, if it is equal to an empty string
    document.getElementById('updateButton').addEventListener('click', function () {
        let myData = {};
        const newValues = document.getElementsByName('newValue');
        const groupValues = document.getElementsByName('groupValue');
        const manualValues = document.getElementsByName('manualValue');

        /*validation
        if(!$('#custodyCharge').val()){
            alert('empty field');
            $('#custodyCharge').focus();
            return;
        }*/

        myData = setValuesToObj(newValues,myData,document);
        myData = setValuesToObj(groupValues,myData,document);
        myData = setManualValuesToObj(manualValues,myData,document, manualInputKeyword);

        //if user changed a currency, but left exchange rate as-is, then send null to calculate automatically
        if (myData.exchangeRate === defaultExchangeRate &&
            (myData.fromCurrency !== defaultFromCurrency || myData.toCurrency !== defaultToCurrency))
            myData.exchangeRate = null;

        //Always get hidden ids of invoice and company
        myData.invoiceId = parseInt(document.getElementById('invoiceId').value);
        myData.companyId = companyId === '' ? null : parseInt(companyId);
        console.log(myData);

        postData('/find/update/execute', myData).then(() =>
        {
            window.location = '/success/updated';
        });
    });
});