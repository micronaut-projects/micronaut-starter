$version = '2.4.4'

$packageArgs = @{
  packageName   = $env:ChocolateyPackageName
  unzipLocation = "$(Split-Path -parent $MyInvocation.MyCommand.Definition)"
  url           = "https://github.com/micronaut-projects/micronaut-starter/releases/download/v$version/mn-win-amd64-v$version.zip"
  checksum      = '43C93E943EE5917395FB042E3ADC24410EB10902A3C26B76576AFAE4485AD312'
  checksumType  = 'sha256'
}

Install-ChocolateyZipPackage @packageArgs
