import { element, by, ElementFinder, ElementArrayFinder } from 'protractor';

import { waitUntilAnyDisplayed, waitUntilDisplayed, click, waitUntilHidden, isVisible } from '../../util/utils';

import NavBarPage from './../../page-objects/navbar-page';

import NEREntityUpdatePage from './ner-entity-update.page-object';

const expect = chai.expect;
export class NEREntityDeleteDialog {
  deleteModal = element(by.className('modal'));
  private dialogTitle: ElementFinder = element(by.id('voicebotApp.nEREntity.delete.question'));
  private confirmButton = element(by.id('jhi-confirm-delete-nEREntity'));

  getDialogTitle() {
    return this.dialogTitle;
  }

  async clickOnConfirmButton() {
    await this.confirmButton.click();
  }
}

export default class NEREntityComponentsPage {
  createButton: ElementFinder = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('div table .btn-danger'));
  title: ElementFinder = element(by.id('ner-entity-heading'));
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
    await navBarPage.getEntityPage('ner-entity');
    await waitUntilAnyDisplayed([this.noRecords, this.table]);
    return this;
  }

  async goToCreateNEREntity() {
    await this.createButton.click();
    return new NEREntityUpdatePage();
  }

  async deleteNEREntity() {
    const deleteButton = this.getDeleteButton(this.records.last());
    await click(deleteButton);

    const nEREntityDeleteDialog = new NEREntityDeleteDialog();
    await waitUntilDisplayed(nEREntityDeleteDialog.deleteModal);
    expect(await nEREntityDeleteDialog.getDialogTitle().getAttribute('id')).to.match(/voicebotApp.nEREntity.delete.question/);
    await nEREntityDeleteDialog.clickOnConfirmButton();

    await waitUntilHidden(nEREntityDeleteDialog.deleteModal);

    expect(await isVisible(nEREntityDeleteDialog.deleteModal)).to.be.false;
  }
}
