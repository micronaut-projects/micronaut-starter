$version = '2.5.6'

$packageArgs = @{
  packageName   = $env:ChocolateyPackageName
  unzipLocation = "$(Split-Path -parent $MyInvocation.MyCommand.Definition)"
  url           = "https://github.com/micronaut-projects/micronaut-starter/releases/download/v$version/mn-win-amd64-v$version.zip"
  checksum      = '4A1D06563A3653EF16D18CCED8DC9F3EEDB396AB095B09AC8E8D4992973FCF3D'
  checksumType  = 'sha256'
}

Install-ChocolateyZipPackage @packageArgs
