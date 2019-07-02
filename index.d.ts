export default class QRReader {
  static readQRCode(): Promise<'CANCEL' | string>;
}