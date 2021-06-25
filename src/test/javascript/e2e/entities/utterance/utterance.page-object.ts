import { element, by, ElementFinder, ElementArrayFinder } from 'protractor';

import { waitUntilAnyDisplayed, waitUntilDisplayed, click, waitUntilHidden, isVisible } from '../../util/utils';

import NavBarPage from './../../page-objects/navbar-page';

import UtteranceUpdatePage from './utterance-update.page-object';

const expect = chai.expect;
export class UtteranceDeleteDialog {
  deleteModal = element(by.className('modal'));
  private dialogTitle: ElementFinder = element(by.id('voicebotApp.utterance.delete.question'));
  private confirmButton = element(by.id('jhi-confirm-delete-utterance'));

  getDialogTitle() {
    return this.dialogTitle;
  }

  async clickOnConfirmButton() {
    await this.confirmButton.click();
  }
}

export default class UtteranceComponentsPage {
  createButton: ElementFinder = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('div table .btn-danger'));
  title: ElementFinder = element(by.id('utterance-heading'));
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
    await navBarPage.getEntityPage('utterance');
    await waitUntilAnyDisplayed([this.noRecords, this.table]);
    return this;
  }

  async goToCreateUtterance() {
    await this.createButton.click();
    return new UtteranceUpdatePage();
  }

  async deleteUtterance() {
    const deleteButton = this.getDeleteButton(this.records.last());
    await click(deleteButton);

    const utteranceDeleteDialog = new UtteranceDeleteDialog();
    await waitUntilDisplayed(utteranceDeleteDialog.deleteModal);
    expect(await utteranceDeleteDialog.getDialogTitle().getAttribute('id')).to.match(/voicebotApp.utterance.delete.question/);
    await utteranceDeleteDialog.clickOnConfirmButton();

    await waitUntilHidden(utteranceDeleteDialog.deleteModal);

    expect(await isVisible(utteranceDeleteDialog.deleteModal)).to.be.false;
  }
}
