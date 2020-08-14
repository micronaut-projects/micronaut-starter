$version = '2.0.1'

$packageArgs = @{
  packageName   = $env:ChocolateyPackageName
  unzipLocation = "$(Split-Path -parent $MyInvocation.MyCommand.Definition)"
  url           = "https://github.com/micronaut-projects/micronaut-starter/releases/download/v$version/mn-win-amd64-v$version.zip"
  checksum      = 'AAFBF8C0FFA997C06D16EAF27C33674EB807FC146F6989A99113D24F94987657'
  checksumType  = 'sha256'
}

Install-ChocolateyZipPackage @packageArgs