$version = '5.2.6'

$packageArgs = @{
  packageName   = $env:ChocolateyPackageName
  unzipLocation = "$(Split-Path -parent $MyInvocation.MyCommand.Definition)"
  url           = "https://github.com/micronaut-projects/micronaut-starter/releases/download/v$version/mn-win-amd64-v$version.zip"
  checksum      = '76CBE3068967CC04B82F670688B93A84F919E78F7EDC07C0EA3D1722A283E61D'
  checksumType  = 'sha256'
}

Install-ChocolateyZipPackage @packageArgs
