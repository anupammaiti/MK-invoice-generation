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
 * POST REQUEST TO THE 'UPDATE' INVOICE CONTROLLER
 * The checkbox corresponding to an element should have the element's id as its own id, but with
 * the word box attached to it. e.g. element-> id="client" & checkbox-> id="clientBox"
 * Id of element is retrieved by removing the string 'Box' from the Id of current checkbox.
 * IF user checks box, they can edit value of corresponding element. If they uncheck the box,
 * element will be assigned its initial value.
 * TODO if manual contains data, then send that data
 */
document.addEventListener('DOMContentLoaded', function () {
    const checkboxes = document.getElementsByName('editBox');

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
        //This conditional will be entered if the checkbox will trigger 1 input, manual or otherwise
        //Checkbox id is the same as element Id, but has the string 'Box' to the end. e.g. 'portfolioBox'
        else {
            let elementId = (checkboxes[i].id).slice(0,-3); //invoiceNumber
            let divId = elementId+'Div'; //invoiceNumberDiv
            //MANUAL ID COMMENTED OUT, MIGHT BE USED WHEN USER CAN CHOOSE BOTH FROM MANUAL OR DROPDOWN LIST
            // if(document.getElementById(elementId) === null  || document.getElementById(elementId)==='undefined')
            //     elementId = elementId+'Manual'; //invoiceNumberManual
            let defaultValue = document.getElementById(elementId).value;
            checkboxes[i].onclick = function () {
                if(checkboxes[i].checked){
                    document.getElementById(divId).style.display = 'block';
                }
                else {
                    document.getElementById(divId).style.display = 'none';
                    document.getElementById(elementId).value = defaultValue;
                }
            }
        }
    }

    //Script will set value of variable as null, if it is equal to an empty string
    document.getElementById('updateButton').addEventListener('click', function () {
        this.disabled = true;
        const newValues = document.getElementsByName('newValue');
        const groupValues = document.getElementsByName('groupValue');
        let myData = {};
        for(let i=0; i<newValues.length; i++) {
            let elementId = newValues[i].id;
            let input = document.getElementById(elementId).value;
            if(input==='')
                myData[elementId] = null;
            else
                myData[elementId] = input;
        }
        for(let i=0; i<groupValues.length; i++) {
            let elementId = groupValues[i].id;
            let input = document.getElementById(elementId).value;
            if(input==='')
                myData[elementId] = null;
            else
                myData[elementId] = input;
        }
        //ADD LOOP HERE FOR MANUAL INPUT. IF MANUAL IS SET, IT WILL OVERRIDE SELECTION
        myData.invoiceId = parseInt(document.getElementById('invoiceId').value);
        myData.companyId = parseInt(document.getElementById('companyId').value);

        console.log(myData);

        postData('/find/update/execute', myData).then(() =>
        {
            window.location = '/success/updated';
        });
        this.disabled = false;

    });
});