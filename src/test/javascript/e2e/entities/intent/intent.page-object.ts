import { element, by, ElementFinder, ElementArrayFinder } from 'protractor';

import { waitUntilAnyDisplayed, waitUntilDisplayed, click, waitUntilHidden, isVisible } from '../../util/utils';

import NavBarPage from './../../page-objects/navbar-page';

import IntentUpdatePage from './intent-update.page-object';

const expect = chai.expect;
export class IntentDeleteDialog {
  deleteModal = element(by.className('modal'));
  private dialogTitle: ElementFinder = element(by.id('voicebotApp.intent.delete.question'));
  private confirmButton = element(by.id('jhi-confirm-delete-intent'));

  getDialogTitle() {
    return this.dialogTitle;
  }

  async clickOnConfirmButton() {
    await this.confirmButton.click();
  }
}

export default class IntentComponentsPage {
  createButton: ElementFinder = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('div table .btn-danger'));
  title: ElementFinder = element(by.id('intent-heading'));
  noRecords: ElementFinder = element(by.css('#app-view-container .table-responsive div.alert.alert-warning'));
  table: ElementFinder = element(by.css('#app-view-container div.table-responsive > table'));

  records: ElementArrayFinder = this.table.all(by.css('tbody tr'));

  getDetailsButton(record: ElementFinder) {
    return record.element(by.css('a.btn.btn-info.btn-sm'));
  }

  getEditButton(record: ElementFinder) {
    return record.element(by.css('a.btn.btn-primary.btn-sm'));
  }

  getDeleteButton(record: ElementFinder) {
    return record.element(by.css('a.btn.btn-danger.btn-sm'));
  }

  async goToPage(navBarPage: NavBarPage) {
    await navBarPage.getEntityPage('intent');
    await waitUntilAnyDisplayed([this.noRecords, this.table]);
    return this;
  }

  async goToCreateIntent() {
    await this.createButton.click();
    return new IntentUpdatePage();
  }

  async deleteIntent() {
    const deleteButton = this.getDeleteButton(this.records.last());
    await click(deleteButton);

    const intentDeleteDialog = new IntentDeleteDialog();
    await waitUntilDisplayed(intentDeleteDialog.deleteModal);
    expect(await intentDeleteDialog.getDialogTitle().getAttribute('id')).to.match(/voicebotApp.intent.delete.question/);
    await intentDeleteDialog.clickOnConfirmButton();

    await waitUntilHidden(intentDeleteDialog.deleteModal);

    expect(await isVisible(intentDeleteDialog.deleteModal)).to.be.false;
  }
}
