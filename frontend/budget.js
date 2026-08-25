const addExpenseButton=document.getElementById("addExpenseButton");
const categoryInput=document.getElementById("category");
const monthlyAmountInput=document.getElementById("monthlyAmount");
const expenseTableBody = document.getElementById("expenseTableBody");

addExpenseButton.addEventListener("click",()=>{
//we get the inputs
const categoryName= categoryInput.value;
const categoryAmount=monthlyAmountInput.value
//create the row
const row=document.createElement("tr");
//create the three cells in that row
const categoryCell=document.createElement("td");
const amountCell=document.createElement("td");
const removeCell = document.createElement("td");
//put the values into the two cells
categoryCell.textContent=categoryName;
amountCell.textContent=categoryAmount;
//create remove button
const removeButton=document.createElement("button");
removeButton.textContent="Remove";
removeButton.type="button";
//give an event listener to the button
removeButton.addEventListener("click",()=>{
row.remove();
});
//put button inside cell;
removeCell.appendChild(removeButton);
//adding the cells into the row
row.appendChild(categoryCell);
row.appendChild(amountCell);
row.appendChild(removeCell);


//adding the row into the table
expenseTableBody.appendChild(row);
});