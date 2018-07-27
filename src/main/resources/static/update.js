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

const getData = (url = ``) => {
    // Default options are marked with *
    return fetch(url, {
        method: "GET", // *GET, POST, PUT, DELETE, etc.
        mode: "cors", // no-cors, cors, *same-origin
        cache: "no-cache", // *default, no-cache, reload, force-cache, only-if-cached
        credentials: "same-origin", // include, same-origin, *omit
        headers: {
            "Content-Type": "application/json; charset=utf-8",
            // "Content-Type": "application/x-www-form-urlencoded",
        },
        redirect: "follow", // manual, *follow, error
        referrer: "no-referrer", // no-referrer, *client
    })
        .then(response => response.json()) // parses response to JSON
        .catch(error => console.error(`Fetch Error =\n`, error));
};

/*POST REQUEST TO THE 'UPDATE' INVOICE CONTROLLER
    * The checkbox corresponding to an element should have the element's id as its own id, but with
    * the word box attached to it. e.g. element-> id="client" & checkbox-> id="clientBox"
    * Id of element is retrieved by removing the string 'Box' from the Id of current checkbox.
    * IF user checks box, they can edit value of corresponding element. If they uncheck the box,
    * element will be assigned its initial value.
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
        else {
            let elementId = (checkboxes[i].id).slice(0,-3);
            let divId = elementId+'Div';
            let manualId = elementId+'Manual';
            let defaultValue = document.getElementById(elementId).value;
            checkboxes[i].onclick = function () {
                if(checkboxes[i].checked){
                    document.getElementById(divId).style.display = 'block';
                }
                else {
                    document.getElementById(divId).style.display = 'none';
                    document.getElementById(elementId).value = defaultValue;
                    let elem = document.getElementById(manualId);
                    if(elem !== 'undefined' && elem !== null)
                        document.getElementById(manualId).value = '';
                }
            }
        }
    }
    document.getElementById('updateButton').addEventListener('click', function () {
        this.disabled = true;
        const newValues = document.getElementsByName('newValue');
        const groupValues = document.getElementsByName('groupValue');
        let myData = {};
        for(let i=0; i<newValues.length; i++) {
            let elementId = newValues[i].id;
            myData[elementId] = document.getElementById(elementId).value
        }
        for(let i=0; i<groupValues.length; i++) {
            let elementId = groupValues[i].id;
            myData[elementId] = document.getElementById(elementId).value
        }
        myData.id = document.getElementById('invoiceId').value;
        console.log(myData);

        postData('/find/update/execute', myData).then(() =>
        {
            window.location = '/success/updated';
        });

        this.disabled = false;

    });
});

        /*getData(`/invoice/${document.getElementById('selectedInvoice').value}`).then(response=>{
            document.getElementById("editPopup").style.display = 'block';
            document.getElementById('type').innerText = response.invoiceType;
            document.getElementById('clientName').innerText = response.client;
        })*/